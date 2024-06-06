package com.bin.im.common.internal.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.Random;

import static com.bin.im.common.internal.utils.ExceptionUtil.rethrow;
import static java.lang.String.format;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class IOUtil {

    private static final Logger logger = LoggerFactory.getLogger(IOUtil.class);
    private static final Random RANDOM = new Random();

    public static void closeResource(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            logger.error("closeResource failed", e);
        }
    }

    public static void deleteQuietly( File f) {
        try {
            delete(f.toPath());
        } catch (Exception ignore) {

        }
    }


    public static void delete( File f) {
        delete(f.toPath());
    }


    public static void delete( Path path) {
        if (!Files.exists(path, NOFOLLOW_LINKS)) {
            return;
        }
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete " + path, e);
        }
    }


    public static void rename(File fileNow, File fileToBe) {
        if (fileNow.renameTo(fileToBe)) {
            return;
        }
        if (!fileNow.exists()) {
            throw new RuntimeException(format("Failed to rename %s to %s because %s doesn't exist.",
                    fileNow, fileToBe, fileNow));
        }
        if (!fileToBe.exists()) {
            throw new RuntimeException(format("Failed to rename %s to %s even though %s doesn't exist.",
                    fileNow, fileToBe, fileToBe));
        }
        if (!fileToBe.delete()) {
            throw new RuntimeException(format("Failed to rename %s to %s. %s exists and could not be deleted.",
                    fileNow, fileToBe, fileToBe));
        }
        if (!fileNow.renameTo(fileToBe)) {
            throw new RuntimeException(format("Failed to rename %s to %s even after deleting %s.",
                    fileNow, fileToBe, fileToBe));
        }
    }


    public static void move(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("File move failed. Fallbacking to delete&move.", e);
            Files.deleteIfExists(target);
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        }
    }

    public static void moveWithTimeout(Path source, Path target, Duration duration) {
        long endTime = System.nanoTime() + duration.toNanos();
        IOException lastException = null;
        do {
            try {
                move(source, target);
            } catch (IOException e) {
                lastException = e;
                logger.error("File move failed", e);
            }
            if (!Files.exists(source)) {
                return;
            }
            try {
                //random delay up to half a second
                Thread.sleep(RANDOM.nextInt(500));
            } catch (InterruptedException ignore) {

            }
        } while (System.nanoTime() - endTime < 0);
        throw new RuntimeException("File move timed out.", lastException);
    }

    public static String toFileName(String name) {
        return name.replaceAll("[:\\\\/*\"?|<>',]", "_");
    }



    public static String getPath(String... parts) {
        if (parts == null || parts.length == 0) {
            throw new IllegalArgumentException("Parts is null or empty.");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            builder.append(part);

            boolean hasMore = i < parts.length - 1;
            if (!part.endsWith(File.separator) && hasMore) {
                builder.append(File.separator);
            }
        }

        return builder.toString();
    }

    public static File getFileFromResources(String resourceFileName) {
        try {
            URL resource = IOUtil.class.getClassLoader().getResource(resourceFileName);
            //noinspection ConstantConditions
            return new File(resource.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Could not find resource file " + resourceFileName, e);
        }
    }

    public static InputStream getFileFromResourcesAsStream(String resourceFileName) {
        try {
            return IOUtil.class.getClassLoader().getResourceAsStream(resourceFileName);
        } catch (Exception e) {
            throw new RuntimeException("Could not find resource file " + resourceFileName, e);
        }
    }

    /**
     * Simulates a Linux {@code touch} command, by setting the last modified time of given file.
     *
     * @param file the file to touch
     */
    public static void touch(File file) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                fos = new FileOutputStream(file);
            }
            if (!file.setLastModified(System.currentTimeMillis())) {
                throw new RuntimeException("Could not touch file " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw rethrow(e);
        } finally {
            closeResource(fos);
        }
    }



    public static void copy(File source, File target) {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source does not exist");
        }
        if (source.isDirectory()) {
            copyDirectory(source, target);
        } else {
            copyFile(source, target, -1);
        }
    }


    public static void copy(InputStream source, File target) {
        if (!target.exists()) {
            throw new RuntimeException("The target file doesn't exist " + target.getAbsolutePath());
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            byte[] buff = new byte[8192];

            int length;
            while ((length = source.read(buff)) > 0) {
                out.write(buff, 0, length);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while copying InputStream", e);
        } finally {
            closeResource(out);
        }
    }



    public static void copyFile(File source, File target, long sourceCount) {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source does not exist " + source.getAbsolutePath());
        }
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source is not a file " + source.getAbsolutePath());
        }
        if (!target.exists() && !target.mkdirs()) {
            throw new RuntimeException("Could not create the target directory " + target.getAbsolutePath());
        }
        final File destination = target.isDirectory() ? new File(target, source.getName()) : target;
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(destination);
            final FileChannel inChannel = in.getChannel();
            final FileChannel outChannel = out.getChannel();
            final long transferCount = sourceCount > 0 ? sourceCount : inChannel.size();
            inChannel.transferTo(0, transferCount, outChannel);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while copying file", e);
        } finally {
            closeResource(in);
            closeResource(out);
        }
    }

    private static void copyDirectory(File source, File target) {
        if (target.exists() && !target.isDirectory()) {
            throw new IllegalArgumentException("Cannot copy source directory since the target already exists,"
                    + " but it is not a directory");
        }
        final File targetSubDir = new File(target, source.getName());
        if (!targetSubDir.exists() && !targetSubDir.mkdirs()) {
            throw new RuntimeException("Could not create the target directory " + target);
        }
        final File[] sourceFiles = source.listFiles();
        if (sourceFiles == null) {
            throw new RuntimeException("Error occurred while listing directory contents for copy");
        }
        for (File file : sourceFiles) {
            copy(file, targetSubDir);
        }
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            drainTo(is, os);
            return os.toByteArray();
        } finally {
            closeResource(os);
        }
    }

    public static void drainTo(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }


    public static void drainTo(InputStream input, OutputStream output, int len) throws IOException {
        byte[] buffer = new byte[1024];
        int remaining = len;
        while (remaining > 0) {
            int n = input.read(buffer, 0, Math.min(buffer.length, remaining));
            if (n == -1) {
                throw new IOException("Not enough bytes in the input stream");
            }
            output.write(buffer, 0, n);
            remaining -= n;
        }
    }



    public static void drainToLimited(InputStream input, OutputStream output, int limit) throws IOException {
        byte[] buffer = new byte[1024];
        int remaining = limit;
        while (remaining > 0) {
            int n = input.read(buffer, 0, Math.min(buffer.length, remaining));
            if (n < 1) {
                return;
            }

            output.write(buffer, 0, n);
            remaining -= n;
        }
    }






}

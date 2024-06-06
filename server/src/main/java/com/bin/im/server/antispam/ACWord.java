package com.bin.im.server.antispam;



import com.bin.im.server.antispam.ahocorasick.trie.Token;
import com.bin.im.server.antispam.ahocorasick.trie.Trie;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class ACWord {


    public static void main(String[] args) {
        List<String> collect = Stream.of("123",

                "敏感词",
                "敏感词句",

                "薬指",
                "リング",
                "人目忍ぶ恋",
                "選んだ",
                "強い",
                "女",
                "見えて",
                "心",
                "中",
                "切なさ",
                "揺れて",

                "oh no",
                "oh yes",
                "but no",
                "loving you is not right",

                "𤳵").collect(Collectors.toList());
        Trie.TrieBuilder trieBuild = Trie.builder();
        trieBuild.caseInsensitive();
        for (String k : collect) {
            trieBuild.addKeyword(k);
        }
        trieBuild.addKeyword("敏感词")
        .addKeyword("敏感词句");

        Trie trie = trieBuild.build();
        Collection<Token> value = trie.tokenize("Hello敏感词句.,asd#(&𤳵/()12%&123敏gan词321");
        int i = 1;
        for (Token token : value) {
            System.out.print(i+token.getFragment());
            if (token.isMatch()) {
                System.out.print(":"+token.getEmit().getKeyword());
            }
            ++i;
        }
        System.out.println("-------------");
        int intervalMillis = 1000;
        int tc = 200;
        int te = intervalMillis - tc;
        System.out.println(te/intervalMillis);
    }

}

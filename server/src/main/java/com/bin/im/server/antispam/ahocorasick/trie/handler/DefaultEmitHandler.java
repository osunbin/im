package com.bin.im.server.antispam.ahocorasick.trie.handler;



import com.bin.im.server.antispam.ahocorasick.trie.Emit;

import java.util.ArrayList;
import java.util.List;

public class DefaultEmitHandler implements EmitHandler {

    private List<Emit> emits = new ArrayList<Emit>();

    @Override
    public void emit(Emit emit) {
        this.emits.add(emit);
    }

    public List<Emit> getEmits() {
        return this.emits;
    }

}

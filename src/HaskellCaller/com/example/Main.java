package com.example;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

public class Main {
    private static final Arena arena = Arena.ofConfined();
    private static final MethodHandle incr;

    static {
        Linker linker = Linker.nativeLinker();

        Path libExamplePath = Path.of(".", "libExample.so");
        SymbolLookup libExample = SymbolLookup.libraryLookup(libExamplePath, arena);

        // incr
        {
            MemorySegment incr_addr = libExample.find("incr")
                    .orElseThrow(() -> new RuntimeException("Symbol not found: incr"));

            ValueLayout incr_ret = ValueLayout.JAVA_INT;
            ValueLayout incr_arg = ValueLayout.JAVA_INT;
            FunctionDescriptor incr_sig = FunctionDescriptor.of(incr_ret, incr_arg);

            incr = linker.downcallHandle(incr_addr, incr_sig);
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Hello from Java!");
        System.out.println("Calling into Haskell:");
        System.out.printf("libExample.incr(%d) = %d\n", 1, invokeIncr(1));
    }

    private static int invokeIncr(int x) throws Throwable {
        return (int) incr.invokeExact(x);
    }
}
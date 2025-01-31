.PHONY: java-run
java-run: java-compile
	java --module-path modules --enable-native-access=HaskellCaller --module HaskellCaller/com.example.Main

.PHONY: java-compile
java-compile: libExample.so
	javac -d modules --module HaskellCaller --module-source-path src

libExample.so: Example.hs hsbracket.o
# Update "-lHSrts-1.0.2_thr-ghc9.4.8" to match the version of GHC you are using
	ghc -O2 -dynamic -shared -fPIC -o libExample.so Example.hs hsbracket.o -lHSrts-1.0.2_thr-ghc9.4.8

hsbracket.o: hsbracket.c
	ghc -O2 -fPIC -c hsbracket.c

.PHONY: clean
clean:
	rm -f *.o *.hi *.so *_stub.h
	rm -rf modules

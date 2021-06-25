# mobiletest

An example of building a clojure library for iOS with graalvm native-image.

## Prerequisites

1. Download java's arm64 static libraries built for ios. They can be downloaded using `download-deps`

```sh

$ scripts/download-deps
```

2. Setup graalvm and make sure your clojure project is graalvm compatible. https://github.com/BrunoBonacci/graalvm-clojure

Make sure `GRAALVM_HOME` is set and is on your path before starting.

```
export GRAALVM_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java11-VERSION/Contents/Home
export PATH=$GRAALVM_HOME/bin:$PATH
```

3. Install [Graalvm LLVM Backend](https://www.graalvm.org/reference-manual/native-image/LLVMBackend/)

```sh
$ gu install llvm-toolchain
```

## Usage

1. Compile your clojure project

```sh

$ ./scripts/compile-shared

```
This can take a while. Sometimes it gets stuck right after the setup phase:
```
[mobiletest-uber:18736]    classlist:   5,974.31 ms,  0.94 GB
[mobiletest-uber:18736]        (cap):     515.76 ms,  0.94 GB
[mobiletest-uber:18736]        setup:   4,171.70 ms,  0.94 GB
```
If it gets stuck directly after setup and doesn't reach the clinit phase within 3-4 minutes, try stopping the script and restarting `./scripts/compile-shared`.

2. Open the xcode project in xcode/MobileTest/MobileTest.xcodeproj  
3. Build and run


## License

Copyright © 2021 Adrian

Distributed under the GPLv2 License. See LICENSE.

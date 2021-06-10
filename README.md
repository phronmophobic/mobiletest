# mobiletest

An example of building a clojure library for iOS with native-image.

## Prerequisites

1. Download the java's arm64 static libraries built for ios. They can be downloaded using `download-deps`

```sh

scripts/download-deps
```

2. Setup graalvm and make sure your clojure project is graalvm compatible. https://github.com/BrunoBonacci/graalvm-clojure


## Usage

1. Compile your clojure project

```sh

./scripts/compile-shared

```

2. Build your app

a. Open the xcode project in xcode/MobileTest/MobileTest.xcodeproj
b. Build and run


## License

Copyright © 2021 Adrian

Distributed under the GPLv2 License. See LICENSE.

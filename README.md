# Android-Umbrella

<img src="image/umbrella.jpeg" width=850></img>

## Import

Step 1. Add the JitPack repository to your build file

```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

Step 2. Add the dependency

```
	dependencies {
	        compile 'com.github.Yellow5A5:Android-Umbrella:1.0.4@aar'
	}
```


## Introduction
Take the umbrella in case it rains.

<img src="https://ws3.sinaimg.cn/large/006tNc79gy1fowkryxpuyj30ns1ak76l.jpg" width=250></img>
<img src="https://ws2.sinaimg.cn/large/006tNc79gy1fowktd845wj30ns1akgr3.jpg" width=250></img>
<img src="https://ws3.sinaimg.cn/large/006tNc79gy1fowkv2u1ufj30ns1ak0vb.jpg" width=250></img>
<img src="https://ws2.sinaimg.cn/large/006tNc79gy1fowkw1l3zij30ns1aktdn.jpg" width=250></img>
<img src="https://ws3.sinaimg.cn/large/006tNc79gy1fowkwqifx3j30ns1akjwe.jpg" width=250></img>

## Usage

```

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        Umbrella.getInstance()
                .inject(this)
//                .openMonitor()
                .setTargetToDefaultThread()
                .openANRWatchDog(3000);
    }
}

```

If you want to know more, please refer to the sample.



## License

	MIT License
	
	Copyright Yellow5A5 2017 
	
	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.

# LayerProgressBar
![progress_bar1](https://github.com/arctic4x/LayerProgressBar/blob/master/gifs/pb_1.gif "pb1")
![progress_bar2](https://github.com/arctic4x/LayerProgressBar/blob/master/gifs/pb_2.gif "pb2")

# Gradle integration
If you're using Gradle, you can declare this library as a dependency

to your project gradle:
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        ...
```
to your app gradle:
```
dependencies {
    implementation 'com.github.arctic4x:LayerProgressBar:v1.1'
}
```
# Basic usage

```java
...
<myapps.alsaev.com.layerprogressbar.LayerProgressBar
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
...
```
# Attributes
| name|default value|usage in xml|usage in code|
| --- |:---:|---| ---|
| lpb_layerCount	|3			|app:lpb_layerCount="3"|view.layerCount=3|
| lpb_layerGap		|4dp		|app:lpb_layerGap="4dp"|view.layerGap=4|
| lpb_layerWidth	|3dp		|app:lpb_layerWidth="3dp"|view.layerWidth=3|
| lpb_layerLength	|250 (0-360)|app:lpb_layerLength="250"|view.layerLength=250|
| lpb_multiThreading|false		|app:lpb_multiThreading="false"|view.isMultiThreading=false|
| lpb_color			|black		|app:lpb_color="@android:color/black"| - |
| lpb_ovalRadius	|4dp		|app:lpb_ovalRadius="4dp"|view.ovalRadius=4|

# License
```
 Copyright [2018] [Boris Alsaev]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
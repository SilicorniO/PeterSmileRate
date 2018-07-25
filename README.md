# PeterSmileRate
An emoji-liked rating view for Android
Based on TTGEmojiRate for iOS: https://github.com/zekunyan/TTGEmojiRate

## Features
 * Allow to get a rating showing a face.
 * touching up and down it is possible to change the smile, changing the rate.

![alt text](https://raw.githubusercontent.com/SilicorniO/PeterSmileRate/master/screenshots/happy.png)
![alt text](https://raw.githubusercontent.com/SilicorniO/PeterSmileRate/master/screenshots/normal.png)
![alt text](https://raw.githubusercontent.com/SilicorniO/PeterSmileRate/master/screenshots/sad.png)

## Dependencies
 * Android SDK 15

##Installation

At the moment you have to copy the "PeterSmileRate" class to your project.
It will be uploaded to jCenter.

### For Gradle users

NOT for the moment.

### For Maven users

NOT for the moment.

##Usage

1. Add a view into your layout:

   ```
   <com.silicornio.testsmile.PeterSmileRate
           android:id="@+id/peterSmileRate"
           android:layout_width="200dp"
           android:layout_height="200dp" />
   ```

2. Get the view into your code.

  ```java
  PeterSmileRate peterSmileRate = findViewById(R.id.peterSmileRate);
   ```

3. Set the listener to receive events with the rating:

    It returns a value between 0 and 100.

  ```java
  peterSmileRate.setListener(new PeterSmileRate.PeterSmileRatingListener() {
      @Override
      public void onRatingChange(PeterSmileRate peterSmileRating, int rate) {
          Log.d("SMILE", "Rating: " + rate);
      }
  });
  ```

## License

    MIT License

    Copyright (c) 2018 SilicorniO

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

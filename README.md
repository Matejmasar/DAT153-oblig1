# Oblig 3 - Quiz App Extended

## Change log from Oblig 1:
- Remove sharing data as parcelable, and create a DataManager class (extends Application) to store the data.
- Remove DataManager and use Room database to store the data.
- Persist score, photo, and answers on screen rotation.

## Answers to the questions

In output of `./gradlew connectedAndroidTest --info` I found the following:
- the build uses build.gradle file to configure the build on project level and then for app level
- it selects primary task **connectedAndroidTest**
- creates array of tasks to be executed (checks, debugging, etc.)
- the actual testing task is **:app:connectedDebugAndroidTest**
- it states how many tests it is starting and on which device **(Starting 7 tests on Pixel 4a - 13)**
- then it always starts with the test file **(com.example.oblig1.MainActivityTest)**
- the test name **(> test_galleryButtonClicked[Pixel 4a - 13])**
- and status **(SUCCESS)**
- if it fails it shows the error message **(java.lang.RuntimeException: Can't create handler inside thread Thread[Instr: androidx.test.runner.AndroidJUnitRunner,5,main] that has not called Looper.prepare()
  at android.os.Handler.<init>(Handler.java:227))**
- at the end it provides summary **(Tests on Pixel 4a - 13, failed: There was 1 failure(s).)**
- or if all passed **(Total tests 7, passed 7)**
- and generates xml report
- finally, it uninstalls all files

APK used for testing is located in **app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk**. 
Decoding it with **apktool** `apktool decode app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` gave me following structure
```
app-debug-androidTest/
├── AndroidManifest.xml
├── apktool.yml
├── META-INF
├── original
├── res
├── smali
├── smali_classes2
├── smali_classes3
└── unknown
```

The AndroidManifest.xml tells us:
- compile sdk version and version code name 
- platform build version code and name
- package name
- instrumentation setup for AndroidJUnitRunner
- queries that APK uses during testing 
- permissions that APK uses - **REORDER TASKS**
- activities that APK uses for testing

The apktool.yml tells us more about the APK structure, like:
- version of apktool used
- apk file name
- used framework
- min and target sdk version
- package info
- version info
- if resources were compressed
- etc.
## Test description

The UI tests are located in **androidTest** directory. There are 3 files:
```
GalleryActivityTest.kt
MainActivityTest.kt
QuizActivityTest.kt
```

### Main Activity tests
There is a Rule that launches the activity before each test. It uses *IntentTestRule*
Then there are 2 tests:
1. **test_galleryButtonClicked** - checks if the button is clickable and if it opens the GalleryActivity
2. **test_quizButtonClicked** - checks if the button is clickable and if it opens the QuizActivity

Openings are checked by using `intended()`

### Gallery Activity tests
There is a Rule that launches the activity before each test. It uses *IntentTestRule*
Then there are 2 tests:
1. **test_addPhoto** - get current number of items in gallery, launches activity to select a new photo, add description, gets stubbed URI, saves the photo, then checks if number of items in gallery went up by one
2. **test_removePhoto** - get current number of items in gallery, remove the first one, check if number of items went down by 1

### Quiz Activity tests
There is a Rule that launches the activity before each test. It uses *IntentTestRule*
Then there are 2 tests:
1. **test_correctAnswer** - get the correct answer, clicks the button with this answer and checks that score was increased
2. **test_wrongAnswer** - get the correct answer, clicks the button with the wrong answer and checks that score was not increased

QuizActivity had to be modified to allow for testing, I added getter for rightAnswer and buttonDescriptions
PhotoSelectorActivity had to be modified to allow for testing, I wrapped asking for permission to avoid errors with resource photo permission


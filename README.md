

# ContentAccessUtil [![](https://jitpack.io/v/vadim1690/ContentAccessUtil.svg)](https://jitpack.io/#vadim1690/ContentAccessUtil)


This is a simple permission library for Android that allows you to easily request runtime permissions.

# Getting Started

## Installation

1) Add it in your root `build.gradle` at the end of repositories:

	    allprojects {
        repositories {
          ...
          maven { url 'https://jitpack.io' }
          }
        }
      
------------------------------------------------------------------------------------------------------------

2) Add the following dependencies in you app level gradle file if not exists:

	    dependencies {
			implementation 'com.github.vadim1690:ContentAccessUtil:1.0.0.1'
		}

------------------------------------------------------------------------------------------------------------

## Prerequisites

To use ContentAccessUtil, you need to have the following:
0
    * Android Studio installed
    * A minimum of Android API 21

------------------------------------------------------------------------------------------------------------

## Installation

1) Clone or download the `ContentAccessUtil` repository.
2) In Android Studio, select `File > New > Import Module`... and select the `ContentAccessUtil` directory.
3) Add the following dependency to your app module's `build.gradle` file:

        dependencies {
           implementation project(':contentaccessutil')
         }

------------------------------------------------------------------------------------------------------------

## Usage

1) Create a `ContentAccessLifeCycleObserver` instance in your activity and pass the activity result registry as an argument to the constructor:

          public class MainActivity extends AppCompatActivity {
              private ContentAccessLifeCycleObserver mContentAccessLifeCycleObserver;

              @Override
              protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  setContentView(R.layout.activity_main);

                  mContentAccessLifeCycleObserver = new ContentAccessLifeCycleObserver(getActivityResultRegistry());
                  getLifecycle().addObserver(mContentAccessLifeCycleObserver);
              }
          }

2) Call the method you need from the `ContentAccessLifeCycleObserver` instance. For example, to take a picture preview:

        mContentAccessLifeCycleObserver.takePicturePreview(bitmap -> {
              // Do something with the bitmap
          });

   Or to select a file:

        mContentAccessLifeCycleObserver.selectFile(ContentAccessLifeCycleObserver.ALL_FILES, uri -> {
              // Do something with the uri
          });

## Methods

    `takePicturePreview(TakePicturePreviewCallback callback)`
    Takes a picture preview and returns the `Bitmap` to the `callback` function.

   * Parameters
     `callback` - the callback function that takes the `Bitmap` of the picture preview.
     
   * Example
        mContentAccessLifeCycleObserver.takePicturePreview(bitmap -> {
              // Do something with the bitmap
          });
          
## `selectFile(String mimeType, SelectFileCallback callback)`
    Launches an intent to allow the user to select a file of a specific MIME type.
    
  * Parameters
    	  `mimeType` - the desired MIME type of the file to be selected.
     	 `callback` - the callback to be invoked after the user has selected a file.
      
  * Example
    
		  mContentAccessLifeCycleObserver.selectFile(ContentAccessLifeCycleObserver.ALL_FILES, uri -> {
			// Do something with the uri
		    });

    

    

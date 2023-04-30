

# ContentAccessUtil [![](https://jitpack.io/v/vadim1690/ContentAccessUtil.svg)](https://jitpack.io/#vadim1690/ContentAccessUtil)


This is a simple library for Android that allows you to access the phone content.

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

   * Android Studio installed
   * A minimum of Android API 21

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
	  
	  
	  

## File selection
	
   Pass this paramerts in the select file function to determine which mime type to select.
   
* ALL_FILES - All files
* AUDIO_AAC - AAC files
* AUDIO_WAV - WAV files
* IMAGE_JPEG - JPEG files
* IMAGE_PNG - PNG files
* AUDIO_MP3 - MP3 files
* VIDEO_MP4 - MP4 files
* FILE_CSV - CSV files
* FILE_WORD - Word files
* FILE_HTML - HTML files
* FILE_JSON - JSON files
* FILE_PDF - PDF files
* FILE_PPT - PPT files
* FILE_XLS - XLS files


## Main Methods

## `takePicturePreview(TakePicturePreviewCallback callback)`
    
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

    

    

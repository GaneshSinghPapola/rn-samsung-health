## rn-samsung-health

React-Native module to read health data from samsung-health application on android device, using samsung health android sdk.



## Installation


 `npm i -S rn-samsung-health` 



## Getting started

If you are using RN >= 0.60 then no need to do further steps. Thanks to Autolinking feature.

## For manual linking

  - Add to your `{YourApp}/android/settings.gradle`:

```
include ':rn-samsung-health'
        project(':rn-samsung-health').projectDir = new File(settingsDir, '../node_modules/rn-samsung-health/android')
```

  - Add dependency to your `android/app/build.gradle` file:


```java
dependencies {
                ...
               compile project(':rn-samsung-health')         // add this line
        }
```


- Add following code to `android/app/src/main/java/**/MainApplication.java`:


```java
import com.reactnative.samsunghealth.SamsungHealthPackage;  // add this line

public class MainApplication extends Application implements ReactApplication {
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            .....,
            new SamsungHealthPackage(BuildConfig.APPLICATION_ID)  // add this line
            );
    }
}
```

- Add permissions in `android/app/src/main/AndroidManifest.xml`:
```xml
        <application
        <meta-data
          android:name="com.samsung.android.health.permission.read"
          android:value="com.samsung.health.weight;com.samsung.health.step_count;com.samsung.shealth.step_daily_trend;com.samsung.health.height;com.samsung.health.blood_pressure;com.samsung.health.heart_rate;com.samsung.health.sleep;com.samsung.health.body_temperature;" />
```



## Usage


```javascript
    import RNSamsungHealth from 'rn-samsung-health'

    useEffect(()=>{
      health();
    },[])
    const health = async() => {
      try{
        const auth = await RNSamsungHealth.authorize();
        let startDate = new Date().setDate(new Date().getDate()-30); // 30 days back date
        let endDate = new Date().getTime(); //today's date
        let opt = {startDate, endDate};
        const steps = await RNSamsungHealth.getDailyStepCount(opt);

      }catch(error){
        console.log("error ", error)
      }
    }
    // more similar functions are - 
    //getDailyStepCount
    //getHeight
    //getWeight
    //getSleep
    //getCholesterol
    //getBloodPressure
    //getBodyTemprature



    // old method 
    // RNSamsungHealth.authorize((err, res) => {
    //   if (res) {
    //    let startDate = new Date().setDate(new Date().getDate()-30); // 30 days back date
    //    let endDate = new Date().getTime(); //today's date
    //     let opt = {startDate, endDate};
    //     RNSamsungHealth.getDailyStepCount(opt, (err, res) => {
    //       if (err) console.log(err);
    //       if (res) console.log(res);
    //     });




    //   } else console.log(err);
    // });
    
    
    
    
    
```


 
## Developer Mode on Samsung Health app


You need Samsung app certification to access all health data provided by Ssamsung Health application. You can apply for Samsung partner apps for your react-native application. For more details please visit [Samsung Health Android SDK](https://developer.samsung.com/health/android)

For development purpose you can enable the developer mode on Samsung Health app:

Open Samsung Health application

Go to > Settings > About Samsung Health

Tap 10 times on the app version `Version XX.XX`.

The name of the version will be changed to ` *(Developer Mode)* XXXX Version XX.XX` and you'll be able to access S Health data.

Feel free to report [issues](https://github.com/GaneshSinghPapola/rn-samsung-health/issues) here.

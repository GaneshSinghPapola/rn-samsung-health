import { NativeModules, DeviceEventEmitter } from 'react-native';

const samsungHealth = NativeModules.RNSamsungHealth;

// Version 1.0.0

class RNSamsungHealth {
  authorize() {
    return new Promise((resolve, reject) =>
      samsungHealth.connect(
        [samsungHealth.STEP_COUNT],
        msg => reject(msg),
        res => resolve(res)
      )
    );
  }

  stop() {
    samsungHealth.disconnect();
  }

  getDailyStepCount(options, callback) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readStepCount(
        startDate,
        endDate,
        msg => reject(msg, false),
        res => {
          if (res.length > 0) {
            var resData = res.map(function(dev) {
              var obj = {};
              obj.source = dev.source.name;
              obj.data = this.buildDailySteps(dev.data);
              obj.sourceDetail = dev.source;
              return obj;
            }, this);

            resolve(resData);
          } else {
            reject('There is no any steps data for this period');
          }
        }
      );
    });
  }

  getWeight(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readWeight(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getSleep(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readSleep(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getHeartRate(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readHeartRate(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getBodyTemprature(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readBodyTemprature(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getBloodPressure(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readBloodPressure(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getHeight(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readHeight(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getWaterIntake(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readWaterIntake(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getNutrition(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readNutrition(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  getCholesterol(options, callback) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readCholesterol(
        startDate,
        endDate,
        msg => reject(msg),
        res => resolve(res)
      );
    });
  }

  usubscribeListeners() {
    DeviceEventEmitter.removeAllListeners();
  }

  buildDailySteps(data) {
    var results = [];
    for (var step of data) {
      var date =
        step.start_time !== undefined
          ? new Date(step.start_time)
          : new Date(step.day_time);

      var day = ('0' + date.getDate()).slice(-2);
      var month = ('0' + (date.getMonth() + 1)).slice(-2);
      var year = date.getFullYear();
      var dateFormatted = year + '-' + month + '-' + day;
      results.push({
        steps: step.count,
        date: dateFormatted,
        calorie: step.calorie
      });
    }
    return results;
  }

  mergeResult(res) {
    results = {};
    for (var dev of res) {
      if (!(dev.sourceDetail.group in results)) {
        results[dev.sourceDetail.group] = {
          source: dev.source,
          sourceDetail: { group: dev.sourceDetail.group },
          stepsDate: {}
        };
      }

      let group = results[dev.sourceDetail.group];

      for (var step of dev.steps) {
        if (!(step.date in group.stepsDate)) {
          group.stepsDate[step.date] = 0;
        }

        group.stepsDate[step.date] += step.value;
      }
    }

    results2 = [];
    for (var index in results) {
      let group = results[index];
      var steps = [];
      for (var date in group.stepsDate) {
        steps.push({
          date: date,
          value: group.stepsDate[date]
        });
      }
      group.steps = steps.sort((a, b) => (a.date < b.date ? -1 : 1));
      delete group.stepsDate;

      results2.push(group);
    }

    return results2;
  }
}

export default new RNSamsungHealth();

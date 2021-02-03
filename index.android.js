import { NativeModules, DeviceEventEmitter } from "react-native";

const samsungHealth = NativeModules.RNSamsungHealth;

// Version 1.0.0

class RNSamsungHealth {
  async authorize() {
    const permissions = samsungHealth.getConstants();

    let permission = [];

    for (const item in permissions) {
      permission.push(permissions[item]);
    }

    if (Array.isArray(permission)) {
      return samsungHealth.connect(permission);
    } else {
      throw " permissions is not array ";
    }
  }

  stop() {
    samsungHealth.disconnect();
  }

  getStepCountDailies(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readStepCountDailies(
        startDate,
        endDate,
        (msg) => reject(msg, false),
        (res) => resolve(res)
      );
    });
  }

  getStepCountSamples(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readStepCountSamples(
        startDate,
        endDate,
        (msg) => reject(msg, false),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
      );
    });
  }

  getExercise(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readExercise(
        startDate,
        endDate,
        (msg) => reject(msg),
        (res) => resolve(res)
      );
    });
  }

  getFloorsClimbed(options) {
    let startDate =
      options.startDate != undefined
        ? options.startDate
        : new Date().setHours(0, 0, 0, 0);
    let endDate =
      options.endDate != undefined ? options.endDate : new Date().valueOf();

    return new Promise((resolve, reject) => {
      samsungHealth.readFloorsClimbed(
        startDate,
        endDate,
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
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
        (msg) => reject(msg),
        (res) => resolve(res)
      );
    });
  }

  usubscribeListeners() {
    DeviceEventEmitter.removeAllListeners();
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

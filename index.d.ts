declare module 'rn-samsung-health' {
  export const authorize: () => Promise<boolean>;

  interface IOptionData {
    startDate: number;
    endDate: number;
  }
  interface ISourceDetailes {
    group: string;
    manufacturer: string;
    model: string;
    name: string;
    uuid: string;
  }

  interface IStepCountDailiesData {
    data: {
      calorie: number;
      day_time: number;
      count: number;
      distance: number;
      speed: number;
    }[];
    source: ISourceDetailes;
  }

  interface IExerciseData {
    data: {
      calorie: number;
      distance: number;
      duration: number;
      day_time: number;
      end_time: number;
      exercise_type: number;
      start_time: number;
      time_offset: number;
    }[];
    source: ISourceDetailes;
  }

  interface IFloorsClimbedData {
    data: {
      floor: number;
      start_time: number;
      time_offset: number;
    }[];
    source: ISourceDetailes;
  }

  interface IStepCountSamplesData {
    data: {
      calorie: number;
      count: number;
      distance: number;
      end_time: number;
      speed: number;
      start_time: number;
      time_offset: number;
    }[];
    source: string;
    sourceDetail: ISourceDetailes;
  }

  export interface IWeightData {
    data: {
      start_time: number;
      time_offset: number;
      weight: number; // Weight in kilograms.
    }[];
    source: ISourceDetailes;
  }

  export interface ISleepData {
    data: {
      end_time: number;
      start_time: number;
      time_offset: number;
    }[];
    source: ISourceDetailes;
  }

  export interface IBloodPressureData {
    data: {
      diastolic: number;
      mean: number;
      start_time: number;
      systolic: number;
      time_offset: number;
    }[];
    source: ISourceDetailes;
  }

  // TODO check this props types
  export interface IBodyTempratureData {
    data: {
      temperature: string; // in Celsius
      start_time: number;
      time_offset: number;
    };
    source: ISourceDetailes;
  }

  export interface IHeartRateData {
    data: {
      end_time: number;
      heart_rate: number; //  beats per minute (bpm).
      start_time: number;
      time_offset: number;
    }[];
    source: ISourceDetailes;
  }

  export interface IHeightData {
    data: {
      height: string; // allways in cm
      start_time: string;
      time_offset: string;
    }[];
    source: ISourceDetailes;
  }

  // TODO check this props types
  export interface ICholesterolData {
    data: {
      total_cholesterol: string; // in mg/dL
      start_time: string;
      time_offset: string;
    }[];
    source: ISourceDetailes;
  }

  export interface IWaterIntakeData {
    data: {
      amount: number; // Amount of water intake in milliliters.
      start_time: string;
      time_offset: string;
    }[];
    source: ISourceDetailes;
  }

  // TODO add props
  export interface INutritionData {
    data: {
      calorie: number;
      start_time: number;
      time_offset: number;
      carbohydrate: number;
      cholesterol: number;
      meal_type: number;
      protein: number;
      saturated_fat: number;
    }[];
    source: ISourceDetailes;
  }

  export const getStepCountDailies: (
    options: IOptionData
  ) => Promise<IStepCountDailiesData[]>;

  export const getStepCountSamples: (
    options: IOptionData
  ) => Promise<IStepCountSamplesData[]>;

  export const getWeight: (options: IOptionData) => Promise<IWeightData[]>;

  export const getSleep: (options: IOptionData) => Promise<ISleepData[]>;

  export const getHeartRate: (
    options: IOptionData
  ) => Promise<IHeartRateData[]>;

  export const getBodyTemprature: (
    options: IOptionData
  ) => Promise<IBodyTempratureData[]>;

  export const getBloodPressure: (
    options: IOptionData
  ) => Promise<IBloodPressureData[]>;

  export const getHeight: (options: IOptionData) => Promise<IHeightData[]>;

  export const getCholesterol: (
    options: IOptionData
  ) => Promise<ICholesterolData[]>;

  export const getWaterIntake: (
    options: IOptionData
  ) => Promise<IWaterIntakeData[]>;

  export const getNutrition: (
    options: IOptionData
  ) => Promise<INutritionData[]>;

  export const getExercise: (options: IOptionData) => Promise<IExerciseData[]>;

  export const getFloorsClimbed: (
    options: IOptionData
  ) => Promise<IFloorsClimbedData[]>;
}


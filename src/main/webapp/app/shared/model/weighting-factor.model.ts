import { ICalculation } from 'app/shared/model/calculation.model';
import { IProperty } from 'app/shared/model/property.model';

export interface IWeightingFactor {
  id?: number;
  value?: number;
  calculation?: ICalculation;
  properties?: IProperty[];
}

export class WeightingFactor implements IWeightingFactor {
  constructor(public id?: number, public value?: number, public calculation?: ICalculation, public properties?: IProperty[]) {}
}

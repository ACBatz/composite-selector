import { IComposite } from 'app/shared/model/composite.model';
import { ICalculation } from 'app/shared/model/calculation.model';

export interface ICalculationResult {
  id?: number;
  ratingFactor?: number;
  composite?: IComposite;
  calculation?: ICalculation;
}

export class CalculationResult implements ICalculationResult {
  constructor(public id?: number, public ratingFactor?: number, public composite?: IComposite, public calculation?: ICalculation) {}
}

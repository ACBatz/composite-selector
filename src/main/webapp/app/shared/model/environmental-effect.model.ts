import { ICalculation } from 'app/shared/model/calculation.model';

export interface IEnvironmentalEffect {
  id?: number;
  name?: string;
  calculations?: ICalculation[];
}

export class EnvironmentalEffect implements IEnvironmentalEffect {
  constructor(public id?: number, public name?: string, public calculations?: ICalculation[]) {}
}

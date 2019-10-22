import { ICalculation } from 'app/shared/model/calculation.model';

export interface IVerb {
  id?: number;
  name?: string;
  calculations?: ICalculation;
}

export class Verb implements IVerb {
  constructor(public id?: number, public name?: string, public calculations?: ICalculation) {}
}

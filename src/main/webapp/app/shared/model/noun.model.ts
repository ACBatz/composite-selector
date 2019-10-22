import { ICalculation } from 'app/shared/model/calculation.model';

export interface INoun {
  id?: number;
  name?: string;
  calculations?: ICalculation;
}

export class Noun implements INoun {
  constructor(public id?: number, public name?: string, public calculations?: ICalculation) {}
}

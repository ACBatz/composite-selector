import { ICalculationResult } from 'app/shared/model/calculation-result.model';
import { IProperty } from 'app/shared/model/property.model';

export interface IComposite {
  id?: number;
  name?: string;
  result?: ICalculationResult;
  properties?: IProperty[];
}

export class Composite implements IComposite {
  constructor(public id?: number, public name?: string, public result?: ICalculationResult, public properties?: IProperty[]) {}
}

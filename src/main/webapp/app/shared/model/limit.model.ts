import { ICalculation } from 'app/shared/model/calculation.model';
import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { IProperty } from 'app/shared/model/property.model';

export interface ILimit {
  id?: number;
  maximum?: boolean;
  value?: number;
  calculations?: ICalculation[];
  unitOfMeasures?: IUnitOfMeasure[];
  properties?: IProperty[];
}

export class Limit implements ILimit {
  constructor(
    public id?: number,
    public maximum?: boolean,
    public value?: number,
    public calculations?: ICalculation[],
    public unitOfMeasures?: IUnitOfMeasure[],
    public properties?: IProperty[]
  ) {
    this.maximum = this.maximum || false;
  }
}

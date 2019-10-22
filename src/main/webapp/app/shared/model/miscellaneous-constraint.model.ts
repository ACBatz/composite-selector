import { ICalculation } from 'app/shared/model/calculation.model';
import { IProperty } from 'app/shared/model/property.model';

export interface IMiscellaneousConstraint {
  id?: number;
  name?: string;
  maximize?: boolean;
  minimize?: boolean;
  calculations?: ICalculation[];
  properties?: IProperty[];
}

export class MiscellaneousConstraint implements IMiscellaneousConstraint {
  constructor(
    public id?: number,
    public name?: string,
    public maximize?: boolean,
    public minimize?: boolean,
    public calculations?: ICalculation[],
    public properties?: IProperty[]
  ) {
    this.maximize = this.maximize || false;
    this.minimize = this.minimize || false;
  }
}

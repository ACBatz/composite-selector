import { ICalculation } from 'app/shared/model/calculation.model';
import { ILoadShear } from 'app/shared/model/load-shear.model';
import { ILoadDirection } from 'app/shared/model/load-direction.model';

export interface ILoad {
  id?: number;
  name?: string;
  isTensileOrCompressive?: boolean;
  isShear?: boolean;
  calculations?: ICalculation[];
  loadShears?: ILoadShear[];
  loadDirections?: ILoadDirection[];
}

export class Load implements ILoad {
  constructor(
    public id?: number,
    public name?: string,
    public isTensileOrCompressive?: boolean,
    public isShear?: boolean,
    public calculations?: ICalculation[],
    public loadShears?: ILoadShear[],
    public loadDirections?: ILoadDirection[]
  ) {
    this.isTensileOrCompressive = this.isTensileOrCompressive || false;
    this.isShear = this.isShear || false;
  }
}

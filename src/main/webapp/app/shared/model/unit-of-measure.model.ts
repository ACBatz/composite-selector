import { ILimit } from 'app/shared/model/limit.model';

export interface IUnitOfMeasure {
  id?: number;
  name?: string;
  limits?: ILimit;
}

export class UnitOfMeasure implements IUnitOfMeasure {
  constructor(public id?: number, public name?: string, public limits?: ILimit) {}
}

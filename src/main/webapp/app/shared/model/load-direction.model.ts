import { ILoad } from 'app/shared/model/load.model';

export const enum LoadDirectionEnum {
  LONGITUDINAL = 'LONGITUDINAL',
  TRANSVERSE = 'TRANSVERSE'
}

export interface ILoadDirection {
  id?: number;
  loadDirectionEnum?: LoadDirectionEnum;
  loads?: ILoad[];
}

export class LoadDirection implements ILoadDirection {
  constructor(public id?: number, public loadDirectionEnum?: LoadDirectionEnum, public loads?: ILoad[]) {}
}

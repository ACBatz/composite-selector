import { ILoad } from 'app/shared/model/load.model';

export const enum LoadShearEnum {
  INPLANE = 'INPLANE',
  INTERLAMINAR = 'INTERLAMINAR'
}

export interface ILoadShear {
  id?: number;
  loadShearEnum?: LoadShearEnum;
  loads?: ILoad[];
}

export class LoadShear implements ILoadShear {
  constructor(public id?: number, public loadShearEnum?: LoadShearEnum, public loads?: ILoad[]) {}
}

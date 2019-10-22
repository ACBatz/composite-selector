import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';
import { ILimit } from 'app/shared/model/limit.model';
import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { IComposite } from 'app/shared/model/composite.model';

export interface IProperty {
  id?: number;
  name?: string;
  weightingFactors?: IWeightingFactor;
  limits?: ILimit;
  miscellaneousConstraint?: IMiscellaneousConstraint;
  composites?: IComposite[];
}

export class Property implements IProperty {
  constructor(
    public id?: number,
    public name?: string,
    public weightingFactors?: IWeightingFactor,
    public limits?: ILimit,
    public miscellaneousConstraint?: IMiscellaneousConstraint,
    public composites?: IComposite[]
  ) {}
}

import { ICalculationResult } from 'app/shared/model/calculation-result.model';
import { IWeightingFactor } from 'app/shared/model/weighting-factor.model';
import { INoun } from 'app/shared/model/noun.model';
import { IVerb } from 'app/shared/model/verb.model';
import { ILimit } from 'app/shared/model/limit.model';
import { IEnvironmentalEffect } from 'app/shared/model/environmental-effect.model';
import { IMiscellaneousConstraint } from 'app/shared/model/miscellaneous-constraint.model';
import { ILoad } from 'app/shared/model/load.model';

export interface ICalculation {
  id?: number;
  name?: string;
  description?: string;
  results?: ICalculationResult[];
  weightingFactors?: IWeightingFactor[];
  nouns?: INoun[];
  verbs?: IVerb[];
  limits?: ILimit[];
  environmentalEffects?: IEnvironmentalEffect[];
  miscConstraints?: IMiscellaneousConstraint[];
  loads?: ILoad[];
}

export class Calculation implements ICalculation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public results?: ICalculationResult[],
    public weightingFactors?: IWeightingFactor[],
    public nouns?: INoun[],
    public verbs?: IVerb[],
    public limits?: ILimit[],
    public environmentalEffects?: IEnvironmentalEffect[],
    public miscConstraints?: IMiscellaneousConstraint[],
    public loads?: ILoad[]
  ) {}
}

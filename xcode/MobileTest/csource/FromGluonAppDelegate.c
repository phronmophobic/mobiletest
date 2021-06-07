//
//  FromGluonAppDelegate.c
//  MobileTest
//
//  Created by Adrian Smith on 6/7/21.
//

#include <stdio.h>


typedef struct {
  char fFP;
  char fASIMD;
  char fEVTSTRM;
  char fAES;
  char fPMULL;
  char fSHA1;
  char fSHA2;
  char fCRC32;
  char fLSE;
  char fSTXRPREFETCH;
  char fA53MAC;
  char fDMBATOMICS;
} CPUFeatures;

void determineCPUFeatures(CPUFeatures* features)
{
    fprintf(stderr, "\n\n\ndetermineCpuFeaures\n");
    features->fFP = 1;
    features->fASIMD = 1;
}

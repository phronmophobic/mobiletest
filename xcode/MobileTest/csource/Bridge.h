//
//  Bridge.h
//  MobileTest
//
//  Created by Adrian Smith on 6/7/21.
//

#ifndef Bridge_h
#define Bridge_h

#include <stdio.h>

#ifdef __cplusplus
extern "C"{
#endif

long long int call_sub(long long int a, long long int b);
long long int call_add(long long int a, long long int b);
void call_print(const char* s);
void call_print_hi(void);
#ifdef __cplusplus
}
#endif

#endif /* Bridge_h */

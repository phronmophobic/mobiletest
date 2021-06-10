//
//  Bridge.c
//  MobileTest
//
//  Created by Adrian Smith on 6/7/21.
//

#include "Bridge.h"


#include "mobiletest-uber.h"

graal_isolate_t *isolate = NULL;
graal_isolatethread_t *thread = NULL;

long long int call_sub(long long int a, long long int b){

    if ( !isolate ){
      if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return 1;
      }
    }

  return clj_sub(thread, a, b);
}


long long int call_add(long long int a, long long int b){

    if ( !isolate ){
      if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return 1;
      }
    }

  return clj_add(thread, a, b);
}

void call_print(const char* s){
    
    if ( !isolate ){
      if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return;
      }
    }

  clj_print(thread,(void*)s);
}
void call_print_hi(void){
    if ( !isolate ){
      if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return;
      }
    }

  clj_print_hi(thread);
}

void call_eval(const char* s){
    
    if ( !isolate ){
      if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "initialization error\n");
        return;
      }
    }

  clj_eval(thread,(void*)s);
}


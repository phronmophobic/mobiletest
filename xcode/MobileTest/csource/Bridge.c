//
//  Bridge.c
//  MobileTest
//
//  Created by Adrian Smith on 6/7/21.
//

#include "Bridge.h"


#include "mobiletest-uber.h"


long long int call_sub(long long int a, long long int b){
  graal_isolate_t *isolate = NULL;
  graal_isolatethread_t *thread = NULL;

  if (graal_create_isolate(NULL, &isolate, &thread) != 0) {
    fprintf(stderr, "initialization error\n");
    return 1;
  }

//  initialize_avclj(thread);
//  cout << "initialized? " << is_avclj_initialized(thread) << endl;
//  long encoder = make_h264_encoder(thread, 256, 256,
//                   (void*) "libavclj.mp4",
//                   (void*) "AV_PIX_FMT_RGB24");
//  cout << "got encoder: " << encoder << endl;
//  int n_bytes = 256 * 256 * 3;
//  char* image = (char*)malloc(n_bytes);
//  for( int frame = 0; frame < 300; ++frame ) {
//    make_frame(image, 256, 256, 3, frame);
//    encode_frame(thread, encoder, image, n_bytes);
//  }
//  close_encoder(thread, encoder);
  return clj_sub(thread, a, b);
}

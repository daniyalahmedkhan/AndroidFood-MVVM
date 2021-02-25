#include <jni.h>

//UAT BASE URL
JNIEXPORT jstring JNICALL
Java_com_androidfood_mvvm_constants_AppConstants_1Java_getBaseUAT(JNIEnv *env, jclass clazz) {
    return (*env)->NewStringUTF(env, "aHR0cDovL3NhbWhhc2VydmljZXMuY29tL2FwcC9hcGkvdjEv");
}
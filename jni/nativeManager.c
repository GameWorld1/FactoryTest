#include "stdio.h"
#include "nativeManager.h"
#include <android/log.h>
#include<fcntl.h>
#define LOG_TAG "nativeManager"
#define LOGD(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

struct CalFlagMarks{
	char ulCalFlag;
	char  ulCalDscrpt[30];
};
struct navram_cal_flag_struct{
	
	char ulCalAllFlag;
	char ulCalMarkNumber;
	struct CalFlagMarks  calFlagMarks[10];
};

struct navram_cal_flag_struct navram_struct;




void   getCorrectData(){
	char *filepath="/data/nvram/md/NVRAM/CALIBRAT/MPA2_000";//NVRAM_EF_CAL_FLAG_LID

	FILE *dataFile;

	dataFile=fopen(filepath,"rb");
	if(dataFile==NULL){
		return;
	}
	LOGD("-------------read navarm ulCalAllFlag =%d  ulCalMarkNumber=%d dataFile=%d\n",navram_struct.ulCalAllFlag,navram_struct.ulCalMarkNumber,dataFile);
	fread(&navram_struct,sizeof(char),sizeof(navram_struct),dataFile);
	fclose(dataFile);
	LOGD("-------------read navarm ulCalAllFlag =%d  ulCalMarkNumber=%d dataFile=%d\n",navram_struct.ulCalAllFlag,navram_struct.ulCalMarkNumber,dataFile);

} 


JNIEXPORT jint JNICALL Java_com_zzx_factorytest_manager_NativeManager_getCorrectResult
  (JNIEnv * env, jobject obj){
	LOGD("---------------");
	getCorrectData();

	return (int)navram_struct.ulCalAllFlag;
	//return 0;
}

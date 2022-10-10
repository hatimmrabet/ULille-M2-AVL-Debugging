#include <stdio.h>
#include "buggy.h"

long int squared (int number) {
	return number*number;
}

int main() {
	counter = 100;
   
	while (counter >= 0){
		// printf("%d\n", counter);
		#ifdef DEBUG
		if(counter == -1) {
			printf("The counter is less than 0!\n");
			exit(1);
		}
		printf("The counter before: %d\n", counter);
		#endif
		counter--;
		long int squaredCounter = squared(counter);
		printf("The squared counter: %ld\n", squaredCounter);
		#ifdef DEBUG
		printf("The counter after: %d\n", counter);
		#endif
	}

	#ifdef DEBUG
	printf("Program finished successfully\n");
	#endif
	
	return 0;
}

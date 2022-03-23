#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h> 

int main() {

mode_t mode = S_IRWXU; //gebe hier Rechte für den User

int writtenBytes;
int fd;
char* text = "HS Hannover \n";
char* datei = "hsh.txt";
int len = strlen(text);

printf("Datei %s wird geoeffnet\n", datei);

fd = open(datei, O_RDWR|O_SYNC);

printf("Dateideskriptor nummer: %d \n",fd);

if( fd < 0) {
    printf("Fehler");
    return -1;
}

for (int i=0; i<10000; i++) {
   writtenBytes = write(fd, text, len);
}
printf("Es wurden %d Bytes geschrieben! \n",writtenBytes);

close(fd);

return EXIT_SUCCESS;
}
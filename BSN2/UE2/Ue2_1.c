#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h> 



int main() {
    
    int fd;
    char* datei = "test.txt";

    printf("Datei %s wird geoeffnet \n", datei);
    
    fd = open(datei, O_RDONLY);

    if (fd < 0) return -1;

    sleep(30);

    printf("Dateideskriptor fd = %d \n", fd);

    close(fd);

    return EXIT_SUCCESS;

}
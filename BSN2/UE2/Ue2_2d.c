#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h> 



int main() {

    int writtenBytes;
    int fd;
    char* datei = "test.txt";
    char* wort = "Hallo";
    size_t laenge = strlen(wort);

    printf("Datei %s wird geoeffnet \n", datei);

    fd = open(datei, O_RDONLY);

    printf("Dateideskriptor nummer: %d \n",fd);

    if (fd < 0) {
        printf("Fehler beim oeffen!");
        return -1;
    }

    writtenBytes = write(fd, wort, laenge);

    printf("Es wurden %d Bytes geschrieben! \n",writtenBytes);

    close(fd);

    return EXIT_SUCCESS;

}
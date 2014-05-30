#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h> 	

int main()
{
	struct Edge{
		int x;
		int y;
		struct Edge *nxt;
	} *start = NULL, *end = start, *tmp, *tmp1;

	FILE *file;
	int x, y, i, t, N, min, max, iterNum = 100;
	char line [ 128 ], delim[2] = " ";
	double d = 0.85;

	file =fopen("facebook_combined.txt", "r");

	if (file == NULL){
		printf("FILE NOT PRESENT");	
		return 1;
	}

	while ( fgets ( line, sizeof line, file ) != NULL )
	{
		x = atoi(strtok (line,delim));
		y = atoi(strtok (NULL,delim));
		if(x < min)
			min = x;
		if(x > max)
			max = x;
		if(y < min)
			min = y;
		if(y > max)
			max = y;
		tmp = (struct Edge *) malloc (sizeof(struct Edge));
		if(start == NULL)
			start = tmp;
		tmp->x = x;
		tmp->y = y;
		tmp->nxt = NULL;
		if(end != NULL)
			end->nxt = tmp;
		end = tmp;
	}

	fclose(file);

	N = max - min + 1;

	double temp[N], vector[N];
	double **M;
	M = (double**)malloc(sizeof(double*) * N);

	for (i = 0; i < N; ++i)
	{
		M[i] = (double *) malloc(sizeof(double) * N);
		if ( M[i] == NULL){
			printf("Failed to allocate memory");
			exit(1);
		}
	}

	for(x = 0 ; x < N ; x++){
		for(y=0;y<N;y++){
			M[x][y] = 0.0;		
		}
		temp[x] = 0;	
	}

	tmp = start;
	while(tmp != NULL){
		x = tmp->x;
		y = tmp->y;
		M[y][x] = 1;	
		M[x][y] = 1;
		temp[x]++;
		temp[y]++;
		tmp1 = tmp;
		tmp = tmp->nxt;
		free(tmp1);
	}

	for(x = 0 ; x < N ; x++){
		for(y = 0 ; y < N ; y++){
			M[y][x] = M[y][x]/temp[x];		
		}
	}

	for(x = 0 ; x < N ; x++){
		vector[x] = 1.0/N;	
	}
	
       #pragma omp parallel for default(none) shared(vector,M,temp) 
	for(t = 0 ; t < iterNum ; t++){	 
		for(x = 0 ; x < N ; x++){ 
                        double sum=0.0;
			for(y = 0 ; y < N ; y++){
				sum += (M[x][y] * vector[y]);
			}
 		      temp[x] = ( (1-d) / N ) + (d * sum);	
		}

		for(x = 0 ; x < N ; x++)
			vector[x] = temp[x];
	}

	file = fopen("Output_Task1.txt","w");	

	for(x = 0 ; x < N ; x++)
	{
		fprintf(file, "Page Rank for %d is : %lf\n", x, vector[x]);
	}

	fclose(file);
	printf("Task Completed.\n");
	return 1;
}

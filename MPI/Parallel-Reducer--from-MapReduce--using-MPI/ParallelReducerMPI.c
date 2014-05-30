#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<mpi.h>

int main(int argc, char *argv[]){

	struct Pair{
		int key;
		int value;
		struct Pair *next;
	} *start = NULL, *end = start, *temp = start, *temp1;

	int rank, size;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Status status;

	int INVALID_KEY = -1, processornum;
	processornum = size;

	int i, j, index, keyValuePairNum = 0, scatterSize = 0, key, value, *data, *localdata, keyRange = 0, *broadCastedInfo, **output, **input, *data1;
	char str1[100], str2[100], delim[2] = ",";
	FILE *file;

	broadCastedInfo = (int *)malloc(sizeof(int) * 3);

	//READ KEY-VALUE PAIRS FROM FILE IN ROOT PROCESSOR
	if(rank == 0){
		file =fopen("100000_key-value_pairs.csv", "r");

		if(!file){
			printf("File not available");	
		}

		int minKey, maxKey;
		fscanf(file,"%s,%s",str1, str2);//READ "KEY,VALUE" TEXT FROM FILE IN LINE 1.NOT USED IN REDUCTION.
		i = 0;
		fscanf(file, "%d,%d", &key, &value);
		minKey = key;
		maxKey = key;
		do{
			temp = (struct Pair *) malloc (sizeof(struct Pair));
			if(start == NULL)
				start = temp;	
			temp->key = key;
			temp->value = value;
			temp->next = NULL;
			if(end == NULL)
				end = temp;
			else
				end->next = temp;
			end = temp;
			keyValuePairNum = keyValuePairNum + 1;

			if(key < minKey)
				minKey = key;
			if(key > maxKey)
				maxKey = key;
		}while(fscanf(file, "%d,%d", &key, &value) ==  2);

		fclose(file);

		keyRange = maxKey - minKey + 1;

		if((keyValuePairNum%processornum) != 0)	
			scatterSize = ((keyValuePairNum/processornum) + 1) * processornum * 2;
		else if((keyValuePairNum%processornum) == 0)
			scatterSize = keyValuePairNum * 2;

		while(INVALID_KEY >= minKey)
			INVALID_KEY--;

		broadCastedInfo[0] = scatterSize;
		broadCastedInfo[1] = keyRange;
		broadCastedInfo[2] = INVALID_KEY;

		data = (int *) malloc(sizeof(int) * scatterSize);

		temp = start;
		i = 0;
		while(temp != NULL){
			data[i] = temp->key;
			data[i + 1] = temp->value;
			temp1 = temp;		
			temp = temp->next;
			free(temp1);
			i = i + 2;
		}
	}

	//BROADCAST SIZE,KEYRANGE,INVALID_KEY TO ALL PROCESSORS FOR FUTURE USE
	MPI_Bcast(broadCastedInfo, 3, MPI_INT, 0, MPI_COMM_WORLD);

	scatterSize = broadCastedInfo[0];
	keyRange = broadCastedInfo[1];
	INVALID_KEY = broadCastedInfo[2];

	localdata = (int *) malloc ( sizeof(int) * (scatterSize/processornum) );

	//SCATTER DATA TO ALL PROCESSORS
	MPI_Scatter(data, (scatterSize/processornum), MPI_INT, localdata, (scatterSize/processornum), MPI_INT, 0, MPI_COMM_WORLD);

	if(rank ==0)	
		free(data);	

	//1st LOCAL REDUCTION
	for(i=0;i<(scatterSize/processornum);i=i+2){
		for(j=0;j<i;j=j+2){
			if(localdata[i] == localdata[j]){
				localdata[j+1] = localdata[j+1] + localdata[i+1];
				localdata[i] = INVALID_KEY;		
			}		
		}	
	}

	//PREPARING DATA FOR SEND/RECV BEFORE SECOND REDUCTION
	output = (int **) malloc(sizeof(int *) * processornum);
	for (i = 0; i < processornum; ++i)
	{
		output[i] = (int *) malloc(sizeof(int) * (scatterSize/processornum));
		if ( output[i] == NULL){
			printf("Failed to allocate memory");
			exit(1);
		}
	}

	for(i=0;i<processornum;i++){
		for(j=0;j<(scatterSize/processornum);j=j+2){
			output[i][j] = INVALID_KEY;
		}	
	}

	for(i=0;i<(scatterSize/processornum);i=i+2){
		key = localdata[i];
		j = key/(keyRange/processornum);
		output[j][i] = key;
		output[j][i+1] = localdata[i+1];	
	}	

	input = (int **) malloc(sizeof(int *) * processornum);
	for (i = 0; i < processornum; i++)
	{
		input[i] = (int *) malloc(sizeof(int) * (scatterSize/processornum));
		if ( input[i] == NULL){
			printf("Failed to allocate memory");
			exit(1);
		}
	}	

	for(i=0;i<processornum;i++){
		for(j=0;j<(scatterSize/processornum);j=j+2){
			input[i][j] = INVALID_KEY;
		}	
	}

	for(i=0;i<(scatterSize/processornum);i=i+2){
		key = localdata[i];
		j = key/(keyRange/processornum);
		if(j == rank){
			input[j][i] = localdata[i];//Key
			input[j][i+1] = localdata[i+1];//Value
		}
	}

	//SENDING KEYS TO APPROPRIATE PROCESSORS FOR REDUCTION
	for(i=0;i<processornum;i++){
		if(i == rank){
			for(j=0;j<processornum;j++){
				if(j != i){
					MPI_Send(output[j], (scatterSize/processornum), MPI_INT, j, 200, MPI_COMM_WORLD);
				}			
			}
		}
		else
		{
			MPI_Recv(input[i], (scatterSize/processornum), MPI_INT, i, 200, MPI_COMM_WORLD, &status);			
		}	
	}

	free(output);
	free(localdata);

	localdata = (int *)malloc(sizeof(int) * scatterSize);
	for(i=0;i<scatterSize;i=i+2)
		localdata[i] = INVALID_KEY;

	index = 0;
	for(i=0;i<processornum;i++){
		for(j=0;j<(scatterSize/processornum);j=j+2){
			if(input[i][j] != INVALID_KEY){
				localdata[index] = input[i][j];
				localdata[index+1] = input[i][j+1];
				index = index + 2;
			}						
		}	
	}
	free(input);

	//FINAL LOCAL REDUCTION
	for(i=0;i<index;i=i+2){
		for(j=0;j<i;j=j+2){
			if(localdata[i] == localdata[j]){
				localdata[j+1] = localdata[j+1] + localdata[i+1];
				localdata[i] = INVALID_KEY;		
			}		
		}	
	}


	//PREPARE ONLY VALID KEY-VALUE PAIRS FOR OUTPUT
	index = 0;
	for(i=0;i<scatterSize;i=i+2)
		if(localdata[i] != INVALID_KEY)
			index=index+2;


	data = (int *)malloc(sizeof(int) * index);
	for(i=0;i<index;i=i+2)
		data[i] = INVALID_KEY;
	j=0;
	for(i=0;i<scatterSize;i=i+2){
		if(localdata[i] != INVALID_KEY){
			data[j] = localdata[i];
			data[j+1] = localdata[i+1];
			j=j+2;
		}
	}

	scatterSize = j;
	//SORT BEFORE OUTPUT TO FILE
	for(i=0;i<scatterSize;i=i+2){
		for(j=2;j<scatterSize;j=j+2){
			if(data[j-2] > data[j]){
				key = data[j];
				value = data[j+1];
				data[j] = data[j-2];
				data[j+1] = data[j-1];
				data[j-2] = key;
				data[j-1] = value;
			}
		}	
	}

	//SEND REDUCED DATA TO ROOT PROCESSOR FOR WRITING TO FILE
	MPI_Send(&scatterSize, 1, MPI_INT, 0, 300, MPI_COMM_WORLD);
	MPI_Send(data, scatterSize, MPI_INT, 0, 400, MPI_COMM_WORLD);

	//RECEIVE DATA AND WRITE REDUCED KEY-VALUE PAIR DATA TO FILE FROM ROOT PROCESSOR
	if(rank == 0){
		file = fopen("output2.txt","w");
		for(i = 0 ; i < scatterSize ; i=i+2)
		{
			fprintf(file, "%d,%d\n", data[i], data[i+1]);
		}
		for(i=1;i<processornum;i++){
			MPI_Recv(&scatterSize, 1, MPI_INT, i, 300, MPI_COMM_WORLD, &status);
			free(data);
			data = (int *) malloc(sizeof(int) * scatterSize);
			MPI_Recv(data, scatterSize, MPI_INT, i, 400, MPI_COMM_WORLD, &status);
			for(j = 0 ; j < scatterSize ; j=j+2)
			{
				fprintf(file, "%d,%d\n", data[j], data[j+1]);
			}
		}
		free(data);
		fclose(file);
	}

	MPI_Finalize();

	return 0;
}

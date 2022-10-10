#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "anagrams-IAGL.h"

#define WORDS_SIZE 21

void swap_ints(int* a, int* b)
{
  if (*a == *b)
    return;

  *a ^= *b;
  *b ^= *a;
  *a ^= *b;
}

void sort(int* array, int array_size)
{
  int i;
  int minimum;

  for(i = 0; i < array_size - 1; ++i)
  {
    minimum = i;
    int j;
    for(j = i + 1; j < array_size; ++j)
      if(array[j] < array[minimum])
        minimum = j;

    swap_ints(&array[i], &array[minimum]);
  }
}

int word_in_array(char** array, int array_size, char* word)
{
  int i;
  for(i = 0; i < array_size; ++i)
    if(strcmp(array[i], word) == 0)
      return 1;
  return 0;
}

void build_anagram_signature(char* word, char* signature)
{
  int str_size = strlen(word);
  int int_signature[6];
  int i;
  char c;
  for (i = 0; i < str_size; ++i)
    int_signature[i] = (int) word[i];

  sort(int_signature, str_size);

  for (i = 0; i < str_size; ++i)
    signature[i] = (char) int_signature[i];

}

void compute_signatures(char** words, int words_size, char** signatures)
{
  int i;

  for (i = 0; i < words_size; ++i)
    signatures[i] = "NONE";

  for (i = 0; i < words_size; ++i)
  {
    char* signature = (char*) calloc(strlen(words[i]), sizeof(char));
    build_anagram_signature(words[i], signature);
    if(!word_in_array(signatures, words_size, signature))
      signatures[i] = signature;
    else
      free(signature);
  }
}

void print_words(char** words, int words_size)
{
  int i;
  for(i = 0; i < words_size; ++i)
  {
    printf("%s ", words[i]);
  }
}

void print_words_anagrams(char** words, int words_size)
{
  char** signatures = (char**) calloc(words_size, sizeof(char*));
  compute_signatures(words, words_size, signatures);
  char* signature;
  int i = 0;

  while((i < words_size))
  {
    int anagrams_size = 0;
    char** anagrams = (char**) calloc(words_size, sizeof(char));

    signature = signatures[i];
    int j;
    for(j = 0; j < words_size; ++j)
    {
      char* sig = (char*) calloc(strlen(words[j]), sizeof(char));
      build_anagram_signature(words[j], sig);
      if(strcmp(sig, signature) == 0 && !word_in_array(anagrams, anagrams_size, words[j]))
      {
        anagrams[anagrams_size] = words[j];
        ++anagrams_size;
      }
      free(sig);
    }

    if(anagrams_size > 1)
    {
      printf("{ ");
      print_words(anagrams, anagrams_size);
      printf("}\n");
    }
    ++i;
  }

  free(signatures);
}

int main(void)
{
  char** str = (char**) calloc(WORDS_SIZE, sizeof(char*));
  str[0] = "le";
  str[1] = "chien";
  str[2] = "marche";
  str[3] = "vers";
  str[4] = "sa";
  str[5] = "niche";
  str[6] = "et";
  str[7] = "trouve";
  str[8] = "une";
  str[9] = "limace";
  str[10] = "de";
  str[11] = "chine";
  str[12] = "nue";
  str[13] = "pleine";
  str[14] = "de";
  str[15] = "malice";
  str[16] = "qui";
  str[17] = "lui";
  str[18] = "fait";
  str[19] = "du";
  str[20] = "charme";

  print_words_anagrams(str, WORDS_SIZE);

  return 0;
}
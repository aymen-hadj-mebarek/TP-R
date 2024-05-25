#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main (){
    FILE * fich_base = NULL ;
    FILE * fich_temp = NULL ;
    int nbr_propositions, nbr_variables, nbr_clauses, i, but[20], non_but[20];
    char nom_base[20], c;

    printf("Entrez le nom de votre Fichier BC (sans extension) :\n");
    gets(nom_base);
    strcat(nom_base, ".cnf");
    fich_base = fopen(nom_base, "r+");
    
    if (fich_base == NULL) {
        printf("Impossible d'ouvrir BC...\n");
    } else {
        fich_temp = fopen("Temp.cnf", "w+");
        if (fich_temp == NULL) {
            printf("Impossible de transferer BC\n");
        } else {
            fscanf(fich_base, "p cnf %d %d ", &nbr_variables, &nbr_clauses);
            nbr_clauses += 1; // pour rajouter le non_but
            fprintf(fich_temp, "p cnf %d %d\n", nbr_variables, nbr_clauses); // on met les infos dans le fichier qu'on va traiter
        }
        c = fgetc(fich_base);
        while (c != EOF) {
            if (c != EOF) fputc(c, fich_temp);
            c = fgetc(fich_base);
        } // on a recopie le reste de la BC dans le fichier qu'on va traiter

        printf("Liste des variables de la BC (litteraux) :\n");
        printf("1 : A1 (Lion)\t\t2 : A2 (Baleine)\t\t3 : A3 (Serpent)\t\n4 : A4 (Pingouin)\t5 : M1 (Mammifere)\t6 : M2 (Reptile)\t\n7 : M3 (Oiseau)\t8 : C1 (Carnivore)\t9 : C2 (Herbivore)\t\n10 : C3 (Omnivore)\t\n\n");
        printf("Entrez le nombre de litteraux : \n");
        scanf("%d", &nbr_propositions);

        for (i = 1; i < nbr_propositions + 1; i++) {
            printf("Entrez le litteral %d : \n", i);
            scanf("%d", &but[i]);
            if (but[i] > -11 && but[i] < 11)
                non_but[i] = but[i] * (-1); // si les codes sont corrects on prend la negation du but (absurde)
            else
                puts("Erreur, Vous avez entre un code invalide");
        }

        fprintf(fich_temp, "\n"); // completion du fichier traite
        for (i = 1; i < nbr_propositions + 1; i++) fprintf(fich_temp, "%d ", non_but[i]); // Ajout des negations au fichier
        fprintf(fich_temp, "0"); // Ajout des 0 pour marquer la fin
        printf("================================================");

        fclose(fich_temp);
        
        system("ubcsat -alg saps -i Temp.cnf -solve > results.txt"); // execution du solveur
    }


    int termine = 0; // signaler fin affichage
    FILE *fich = fopen("results.txt", "r+");
    if (fich == NULL) {
        printf("Impossible d'acceder aux resultats...\n");
    } else {
        char texte[1000];
        while (fgets(texte, 1000, fich) && !termine) {
            if (strstr(texte, "# Solution found for -target 0")) {
                printf("\nBC U {Non but} est satisfiable. La base n'infere pas le but. \nSolution : \n");
                fscanf(fich, "\n");
                while (!strstr(fgets(texte, 1000, fich), "Variables"))
                    printf("%s", texte);
                termine = 1;
            }
        }
        if (termine == 0) { // cas de non satisfiabilite
            printf("\nBC U {Non but} est non satisfiable. La base infere le but.\n");
            int j;
            for (j = 1; j < nbr_propositions + 1; j++) {
                printf("%d ", (-1) * but[j]);
            }
            if (j > 2)
                printf("ne peuvent pas etre atteints ");
            else
                printf("ne peut pas etre atteint \n");
        }
    }

    fclose(fich);

    return 0;
}

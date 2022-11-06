import matplotlib.pyplot as plt
import numpy as np

def draw(fp1, fp2, fp3, fp4):

    plt.rcParams.update({'font.size': 15})

    x = [140, 200, 260, 320]

    # promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0]]

    # desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=1, markersize=6, capsize=10, color="purple", ecolor='lightgreen', elinewidth=3, label='11')

    ax.set(xlim=(100, 340), xticks=x,
           ylim=(0, 400), yticks=np.arange(0,400,50))

    plt.xlabel('Cantidad de humanos')
    plt.ylabel('Tiempo hasta fz = 1 (s)')

    plt.show()



if __name__ == '__main__':
    aux140 = [344.02, 22]
    aux200 = [196.36, 15]
    aux260 = [176.23, 12]
    aux320 = [136.19, 10]
    draw(aux140,aux200, aux260, aux320)


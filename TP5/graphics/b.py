import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


#grafico del desvio estandar
#a partir de un cantidad de simulaciones calculamos el promedio y el desvio estandar

def draw(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8):

    plt.rcParams.update({'font.size': 12})
    #plt.rcParams["figure.figsize"] = (20,3)


    x = [2, 10, 40, 80, 140, 200, 260, 320]

    #promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0]]

    #desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1], fp5[1], fp6[1], fp7[1], fp8[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2, label='11')

    ax.set(xlim=(-10, 340), xticks=x,
           ylim=(0, 1.2), yticks=np.arange(0, 1.2, 0.1))

    plt.xlabel('Cantidad de humanos')
    plt.ylabel('Fz')

    plt.show()


def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    time = []
    fz = []
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        time.append(float(split[0]))
        fz.append(float(split[1])/float(split[2]))

    return time, fz

if __name__ == '__main__':
    #2
    time2_1, fz2_1 = parseParameters(sys.argv[1])
    time2_2, fz2_2 = parseParameters(sys.argv[2])
    time2_3, fz2_3 = parseParameters(sys.argv[3])
    avgTime2 = mean([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    desvioTime2 = np.std([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    #10
    time10_1, fz10_1 = parseParameters(sys.argv[1])
    time10_2, fz10_2 = parseParameters(sys.argv[2])
    time10_3, fz10_3 = parseParameters(sys.argv[3])
    avgTime10 = mean([fz10_1[-1], fz10_2[-1], fz10_3[-1]])
    desvioTime10 = np.std([fz10_1[-1], fz10_2[-1], fz10_3[-1]])
    #40
    time40_1, fz40_1 = parseParameters(sys.argv[1])
    time40_2, fz40_2 = parseParameters(sys.argv[2])
    time40_3, fz40_3 = parseParameters(sys.argv[3])
    avgTime40 = mean([fz40_1[-1], fz40_2[-1], fz40_3[-1]])
    desvioTime40 = np.std([fz40_1[-1], fz40_2[-1], fz40_3[-1]])
    #80
    time80_1, fz80_1 = parseParameters(sys.argv[1])
    time80_2, fz80_2 = parseParameters(sys.argv[2])
    time80_3, fz80_3 = parseParameters(sys.argv[3])
    avgTime80 = mean([fz80_1[-1], fz80_2[-1], fz80_3[-1]])
    desvioTime80 = np.std([fz80_1[-1], fz80_2[-1], fz80_3[-1]])
    #140
    time140_1, fz140_1 = parseParameters(sys.argv[1])
    time140_2, fz140_2 = parseParameters(sys.argv[2])
    time140_3, fz140_3 = parseParameters(sys.argv[3])
    avgTime140 = mean([fz140_1[-1], fz140_2[-1], fz140_3[-1]])
    desvioTime140 = np.std([fz140_1[-1], fz140_2[-1], fz140_3[-1]])
    #200
    time200_1, fz200_1 = parseParameters(sys.argv[1])
    time200_2, fz200_2 = parseParameters(sys.argv[2])
    time200_3, fz200_3 = parseParameters(sys.argv[3])
    avgTime200 = mean([fz200_1[-1], fz200_2[-1], fz200_3[-1]])
    desvioTime200 = np.std([fz200_1[-1], fz200_2[-1], fz200_3[-1]])
    #320
    time320_1, fz320_1 = parseParameters(sys.argv[1])
    time320_2, fz320_2 = parseParameters(sys.argv[2])
    time320_3, fz320_3 = parseParameters(sys.argv[3])
    avgTime320 = mean([fz320_1[-1], fz320_2[-1], fz320_3[-1]])
    desvioTime320 = np.std([fz320_1[-1], fz320_2[-1], fz320_3[-1]])


    draw([avgTime2,desvioTime2],[avgTime10,desvioTime10],[avgTime40,desvioTime40],[avgTime80,desvioTime80],[avgTime140,desvioTime140], [avgTime200, desvioTime200], [avgTime320, desvioTime320])


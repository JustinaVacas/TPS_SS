import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


#grafico del desvio estandar
#a partir de un cantidad de simulaciones calculamos el promedio y el desvio estandar

def draw(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8, aux40, aux80, aux140):

    #plt.rcParams.update({'font.size': 12})
    #plt.rcParams["figure.figsize"] = (20,3)

    plt.rcParams.update({'font.size': 25})
    plt.rc('figure', figsize=(20, 10))

    x = [2, 10, 40, 80, 140, 200, 260, 320]
    xticks = [0,10, 40, 80, 140, 200, 260, 320]
    #promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0]]

    #desvio estandar
    yerr = [fp1[1], fp2[1], 0, 0, 0, fp6[1], fp7[1], fp8[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=20, markersize='20',capsize=30, color="purple", ecolor='lightgreen', elinewidth=6, label='11')

    ax.set(xlim=(-10, 340), xticks=x,
           ylim=(0, 1.2), yticks=np.arange(0, 1.2, 0.1))

    pos = [40, 80, 140]
    data = [aux40, aux80, aux140]
    ax.violinplot(data, pos, points=20, widths=5,
                  showmeans=False, showextrema=True, showmedians=False)


    plt.xlabel('Cantidad de humanos')
    plt.ylabel('Fz')

    plt.show()


def drawVelocity(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8):

    plt.rcParams.update({'font.size': 25})
    plt.rc('figure', figsize=(20, 10))

    x = [2,10, 40, 80, 140, 200, 260, 320]

    # promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0]]

    # desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1], fp5[1], fp6[1], fp7[1], fp8[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=20, markersize='20',capsize=30, color="purple", ecolor='lightgreen', elinewidth=6, label='11')

    ax.set(xlim=(-10, 340), xticks=x,
           ylim=(-0.1, 1), yticks=np.arange(0, 1.2, 0.1))

    plt.xlabel('Cantidad de humanos (m/s)')
    plt.ylabel('Velocidad de contagio (z/s)')

    plt.show()


def time(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8):

    plt.rcParams.update({'font.size': 25})
    plt.rc('figure', figsize=(20, 10))

    x = [2,10, 40, 80, 140, 200, 260, 320]

    # promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0]]

    # desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1], fp5[1], fp6[1], fp7[1], fp8[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=20, markersize='20',capsize=30, color="purple", ecolor='lightgreen', elinewidth=6, label='11')

    ax.set(xlim=(-10, 340), xticks=x,
           ylim=(-0.1, 1), yticks=np.arange(0, 1.2, 0.1))

    plt.xlabel('Cantidad de humanos (m/s)')
    plt.ylabel('Velocidad de contagio (z/s)')

    plt.show()


def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    time = []
    fz = []
    zombies = []
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        time.append(float(split[0]))
        fz.append(float(split[1])/float(split[2]))
        zombies.append(float(split[1])/300)

    return time, fz, zombies

if __name__ == '__main__':
    #2
    time2_1, fz2_1, zombies2_1 = parseParameters(sys.argv[1])
    time2_2, fz2_2, zombies2_2 = parseParameters(sys.argv[2])
    time2_3, fz2_3, zombies2_3 = parseParameters(sys.argv[3])
    avgTime2 = mean([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    desvioTime2 = np.std([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    avgZombie2 = mean([zombies2_1[-1], zombies2_2[-1], zombies2_3[-1]])
    desvioZombie2 = np.std([zombies2_1[-1], zombies2_2[-1], zombies2_3[-1]])

    # #10
    time10_1, fz10_1, zombies10_1 = parseParameters(sys.argv[4])
    time10_2, fz10_2, zombies10_2 = parseParameters(sys.argv[5])
    time10_3, fz10_3, zombies10_3 = parseParameters(sys.argv[6])
    avgTime10 = mean([fz10_1[-1], fz10_2[-1], fz10_3[-1]])
    desvioTime10 = np.std([fz10_1[-1], fz10_2[-1], fz10_3[-1]])
    avgZombie10 = mean([zombies10_1[-1], zombies10_2[-1], zombies10_3[-1]])
    desvioZombie10 = np.std([zombies10_1[-1], zombies10_2[-1], zombies10_3[-1]])
    #40
    time40_1, fz40_1, zombies40_1 = parseParameters(sys.argv[7])
    time40_2, fz40_2, zombies40_2 = parseParameters(sys.argv[8])
    time40_3, fz40_3, zombies40_3 = parseParameters(sys.argv[9])
    avgTime40 = mean([fz40_1[-1], fz40_2[-1], fz40_3[-1]])
    desvioTime40 = np.std([fz40_1[-1], fz40_2[-1], fz40_3[-1]])
    avgZombie40 = mean([zombies40_1[-1], zombies40_2[-1], zombies40_3[-1]])
    desvioZombie40 = np.std([zombies40_1[-1], zombies40_2[-1], zombies40_3[-1]])
    #80
    time80_1, fz80_1, zombies80_1 = parseParameters(sys.argv[10])
    time80_2, fz80_2, zombies80_2 = parseParameters(sys.argv[11])
    time80_3, fz80_3, zombies80_3 = parseParameters(sys.argv[12])
    avgTime80 = mean([fz80_1[-1], fz80_2[-1], fz80_3[-1]])
    desvioTime80 = np.std([fz80_1[-1], fz80_2[-1], fz80_3[-1]])
    avgZombie80 = mean([zombies80_1[-1], zombies80_2[-1], zombies80_3[-1]])
    desvioZombie80 = np.std([zombies80_1[-1], zombies80_2[-1], zombies80_3[-1]])
    #140
    time140_1, fz140_1, zombies140_1 = parseParameters(sys.argv[13])
    time140_2, fz140_2, zombies140_2 = parseParameters(sys.argv[14])
    time140_3, fz140_3, zombies140_3 = parseParameters(sys.argv[15])
    avgTime140 = mean([fz140_1[-1], fz140_2[-1], fz140_3[-1]])
    desvioTime140 = np.std([fz140_1[-1], fz140_2[-1], fz140_3[-1]])
    avgZombie140 = mean([zombies140_1[-1], zombies140_2[-1], zombies140_3[-1]])
    desvioZombie140 = np.std([zombies140_1[-1], zombies140_2[-1], zombies140_3[-1]])
    #200
    time200_1, fz200_1, zombies200_1 = parseParameters(sys.argv[16])
    time200_2, fz200_2, zombies200_2 = parseParameters(sys.argv[17])
    time200_3, fz200_3, zombies200_3 = parseParameters(sys.argv[18])
    avgTime200 = mean([fz200_1[-1], fz200_2[-1], fz200_3[-1]])
    desvioTime200 = np.std([fz200_1[-1], fz200_2[-1], fz200_3[-1]])
    avgZombie200 = mean([zombies200_1[-1], zombies200_2[-1], zombies200_3[-1]])
    desvioZombie200 = np.std([zombies200_1[-1], zombies200_2[-1], zombies200_3[-1]])
    #260
    time260_1, fz260_1, zombies260_1 = parseParameters(sys.argv[19])
    time260_2, fz260_2, zombies260_2 = parseParameters(sys.argv[20])
    time260_3, fz260_3, zombies260_3 = parseParameters(sys.argv[21])
    avgTime260 = mean([fz260_1[-1], fz260_2[-1], fz260_3[-1]])
    desvioTime260 = np.std([fz260_1[-1], fz260_2[-1], fz260_3[-1]])
    avgZombie260 = mean([zombies260_1[-1], zombies260_2[-1], zombies260_3[-1]])
    desvioZombie260 = np.std([zombies260_1[-1], zombies260_2[-1], zombies260_3[-1]])
    #320
    time320_1, fz320_1, zombies320_1 = parseParameters(sys.argv[22])
    time320_2, fz320_2, zombies320_2 = parseParameters(sys.argv[23])
    time320_3, fz320_3, zombies320_3 = parseParameters(sys.argv[24])
    avgTime320 = mean([fz320_1[-1], fz320_2[-1], fz320_3[-1]])
    desvioTime320 = np.std([fz320_1[-1], fz320_2[-1], fz320_3[-1]])
    avgZombie320 = mean([zombies320_1[-1], zombies320_2[-1], zombies320_3[-1]])
    desvioZombie320 = np.std([zombies320_1[-1], zombies320_2[-1], zombies320_3[-1]])

    aux40 = [fz40_1[-1], fz40_2[-1], fz40_3[-1]]
    aux80 = [fz80_1[-1], fz80_2[-1], fz80_3[-1]]
    aux140 = [fz140_1[-1], fz140_2[-1], fz140_3[-1]]
    drawVelocity([avgZombie2,desvioZombie2],[avgZombie10,desvioZombie10],[avgZombie40,desvioZombie40],[avgZombie80,desvioZombie80],[avgZombie140,desvioZombie140], [avgZombie200, desvioZombie200],[avgZombie260, desvioZombie260], [avgZombie320, desvioZombie320])
    draw([avgTime2,desvioTime2],[avgTime10,desvioTime10],[avgTime40,desvioTime40],[avgTime80,desvioTime80],[avgTime140,desvioTime140], [avgTime200, desvioTime200],[avgTime260, desvioTime260], [avgTime320, desvioTime320], aux40, aux80, aux140)

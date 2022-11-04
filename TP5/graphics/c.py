import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean

def drawVelocity(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8, fp9):
    plt.rcParams.update({'font.size': 12})
    # plt.rcParams["figure.figsize"] = (20,3)

    x = [1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5]

    # promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0], fp9[0]]

    # desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1], fp5[1], fp6[1], fp7[1], fp8[1], fp9[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2,
                label='11')

    ax.set(xlim=(0.5, 5.5), xticks=x,
           ylim=(-0.1, 1), yticks=np.arange(0, 1.2, 0.1))

    plt.xlabel('Velocidad zombie (m/s)')
    plt.ylabel('Velocidad de contagio')

    plt.show()


# grafico del desvio estandar
# a partir de un cantidad de simulaciones calculamos el promedio y el desvio estandar

def draw(fp1, fp2, fp3, fp4, fp5, fp6, fp7, fp8, fp9):
    plt.rcParams.update({'font.size': 12})
    # plt.rcParams["figure.figsize"] = (20,3)

    x = [1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5]

    # promedio
    y = [fp1[0], fp2[0], fp3[0], fp4[0], fp5[0], fp6[0], fp7[0], fp8[0], fp9[0]]

    # desvio estandar
    yerr = [fp1[1], fp2[1], fp3[1], fp4[1], fp5[1], fp6[1], fp7[1], fp8[1], fp9[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2,
                label='11')

    ax.set(xlim=(0.5, 5.5), xticks=x,
           ylim=(-0.1, 1), yticks=np.arange(0, 1.2, 0.1))

    plt.xlabel('Velocidad zombie (m/s)')
    plt.ylabel('Fz')

    plt.show()


def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    time = []
    fz = []
    velocity = []
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        time.append(float(split[0]))
        fz.append(float(split[1]) / float(split[2]))
        velocity.append(float(split[1])/300)

    return time, fz, velocity


if __name__ == '__main__':
    # 1
    time1_1, fz1_1, velocity1_1 = parseParameters(sys.argv[1])
    time1_2, fz1_2, velocity1_2 = parseParameters(sys.argv[2])
    time1_3, fz1_3, velocity1_3 = parseParameters(sys.argv[3])
    avgTime1 = mean([fz1_1[-1], fz1_2[-1], fz1_3[-1]])
    desvioTime1 = np.std([fz1_1[-1], fz1_2[-1], fz1_3[-1]])
    avgVelocity1 = mean([velocity1_1[-1], velocity1_2[-1], velocity1_3[-1]])
    desvioVelocity1 = np.std([velocity1_1[-1], velocity1_2[-1], velocity1_3[-1]])
    # 1.5
    time15_1, fz15_1, velocity15_1 = parseParameters(sys.argv[4])
    time15_2, fz15_2, velocity15_2 = parseParameters(sys.argv[5])
    time15_3, fz15_3, velocity15_3 = parseParameters(sys.argv[6])
    avgTime15 = mean([fz15_1[-1], fz15_2[-1], fz15_3[-1]])
    desvioTime15 = np.std([fz15_1[-1], fz15_2[-1], fz15_3[-1]])
    avgVelocity15 = mean([velocity15_1[-1], velocity15_2[-1], velocity15_3[-1]])
    desvioVelocity15 = np.std([velocity15_1[-1], velocity15_2[-1], velocity15_3[-1]])
    # 2
    time2_1, fz2_1, velocity2_1 = parseParameters(sys.argv[7])
    time2_2, fz2_2, velocity2_2 = parseParameters(sys.argv[8])
    time2_3, fz2_3, velocity2_3 = parseParameters(sys.argv[9])
    avgTime2 = mean([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    desvioTime2 = np.std([fz2_1[-1], fz2_2[-1], fz2_3[-1]])
    avgVelocity2 = mean([velocity2_1[-1], velocity2_2[-1], velocity2_3[-1]])
    desvioVelocity2 = np.std([velocity2_1[-1], velocity2_2[-1], velocity2_3[-1]])
    # 2.5
    time25_1, fz25_1, velocity25_1  = parseParameters(sys.argv[10])
    time25_2, fz25_2, velocity25_2 = parseParameters(sys.argv[11])
    time25_3, fz25_3, velocity25_3 = parseParameters(sys.argv[12])
    avgTime25 = mean([fz25_1[-1], fz25_2[-1], fz25_3[-1]])
    desvioTime25 = np.std([fz25_1[-1], fz25_2[-1], fz25_3[-1]])
    avgVelocity25 = mean([velocity25_1[-1], velocity25_2[-1], velocity25_3[-1]])
    desvioVelocity25 = np.std([velocity25_1[-1], velocity25_2[-1], velocity25_3[-1]])
    # 3
    time3_1, fz3_1, velocity3_1 = parseParameters(sys.argv[13])
    time3_2, fz3_2, velocity3_2 = parseParameters(sys.argv[14])
    time3_3, fz3_3, velocity3_3 = parseParameters(sys.argv[15])
    avgTime3 = mean([fz3_1[-1], fz3_2[-1], fz3_3[-1]])
    desvioTime3 = np.std([fz3_1[-1], fz3_2[-1], fz3_3[-1]])
    avgVelocity3 = mean([velocity3_1[-1], velocity3_2[-1], velocity3_3[-1]])
    desvioVelocity3 = np.std([velocity3_1[-1], velocity3_2[-1], velocity3_3[-1]])
    # 3.5
    time35_1, fz35_1, velocity35_1 = parseParameters(sys.argv[16])
    time35_2, fz35_2, velocity35_2 = parseParameters(sys.argv[17])
    time35_3, fz35_3, velocity35_3 = parseParameters(sys.argv[18])
    avgTime35 = mean([fz35_1[-1], fz35_2[-1], fz35_3[-1]])
    desvioTime35 = np.std([fz35_1[-1], fz35_2[-1], fz35_3[-1]])
    avgVelocity35 = mean([velocity35_1[-1], velocity35_2[-1], velocity35_3[-1]])
    desvioVelocity35 = np.std([velocity35_1[-1], velocity35_2[-1], velocity35_3[-1]])
    # 4
    time4_1, fz4_1, velocity4_1 = parseParameters(sys.argv[19])
    time4_2, fz4_2, velocity4_2 = parseParameters(sys.argv[20])
    time4_3, fz4_3, velocity4_3 = parseParameters(sys.argv[21])
    avgTime4 = mean([fz4_1[-1], fz4_2[-1], fz4_3[-1]])
    desvioTime4 = np.std([fz4_1[-1], fz4_2[-1], fz4_3[-1]])
    avgVelocity4 = mean([velocity4_1[-1], velocity4_2[-1], velocity4_3[-1]])
    desvioVelocity4 = np.std([velocity4_1[-1], velocity4_2[-1], velocity4_3[-1]])
    # 4.5
    time45_1, fz45_1, velocity45_1 = parseParameters(sys.argv[22])
    time45_2, fz45_2, velocity45_2 = parseParameters(sys.argv[23])
    time45_3, fz45_3, velocity45_3 = parseParameters(sys.argv[24])
    avgTime45 = mean([fz45_1[-1], fz45_2[-1], fz45_3[-1]])
    desvioTime45 = np.std([fz45_1[-1], fz45_2[-1], fz45_3[-1]])
    avgVelocity45 = mean([velocity45_1[-1], velocity45_2[-1], velocity45_3[-1]])
    desvioVelocity45 = np.std([velocity45_1[-1], velocity45_2[-1], velocity45_3[-1]])
    # 5
    time5_1, fz5_1, velocity5_1 = parseParameters(sys.argv[25])
    time5_2, fz5_2, velocity5_2 = parseParameters(sys.argv[26])
    time5_3, fz5_3, velocity5_3 = parseParameters(sys.argv[27])
    avgTime5 = mean([fz5_1[-1], fz5_2[-1], fz5_3[-1]])
    desvioTime5 = np.std([fz5_1[-1], fz5_2[-1], fz5_3[-1]])
    avgVelocity5 = mean([velocity5_1[-1], velocity5_2[-1], velocity5_3[-1]])
    desvioVelocity5 = np.std([velocity5_1[-1], velocity5_2[-1], velocity5_3[-1]])

    draw([avgTime1, desvioTime1], [avgTime15, desvioTime15], [avgTime2, desvioTime2], [avgTime25, desvioTime25],
         [avgTime3, desvioTime3], [avgTime35, desvioTime35], [avgTime4, desvioTime4], [avgTime45, desvioTime45],
         [avgTime5, desvioTime5])

    drawVelocity([avgVelocity1, desvioVelocity1], [avgVelocity15, desvioVelocity15], [avgVelocity2, desvioVelocity2], [avgVelocity25, desvioVelocity25],
                 [avgVelocity3, desvioVelocity3], [avgVelocity35, desvioVelocity35], [avgVelocity4, desvioVelocity4], [avgVelocity45, desvioVelocity45],
                 [avgVelocity5, desvioVelocity5])
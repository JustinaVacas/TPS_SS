import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


def draw(orders1, orders2, orders3):

    x = [0.01, 0.02, 0.04]

    y = [orders1[0], orders2[0], orders3[0]]

    yerr = [orders1[1], orders2[1], orders3[1]]

    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2, label='11')

    ax.set(xlim=(0, 0.05), xticks=np.arange(0, 0.06, 0.01),
           ylim=(0, 1.1), yticks=np.arange(0, 600, 50))

    plt.xlabel('D')
    plt.ylabel('Tiempo')

    plt.show()


def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    time = []
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 2:
            continue
        elif len(split) == 3:
            time.append(float(split[0]))

    return time


if __name__ == '__main__':
    time1 = parseParameters(sys.argv[1])
    time2 = parseParameters(sys.argv[2])
    time3 = parseParameters(sys.argv[3])
    avg1 = mean([time1[-1], time2[-1], time3[-1]])
    desvio1 = np.std([time1[-1], time2[-1], time3[-1]])

    print("avg", avg1)
    print("desvio1", desvio1)

    time4 = parseParameters(sys.argv[4])
    time5 = parseParameters(sys.argv[5])
    time6 = parseParameters(sys.argv[6])
    avg2 = mean([time4[-1], time5[-1], time6[-1]])
    desvio2 = np.std([time4[-1], time5[-1], time6[-1]])

    print("avg", avg2)
    print("desvio1", desvio2)

    time7 = parseParameters(sys.argv[7])
    time8 = parseParameters(sys.argv[8])
    time9 = parseParameters(sys.argv[9])
    avg3 = mean([time7[-1], time8[-1], time9[-1]])
    desvio3 = np.std([time7[-1], time8[-1], time9[-1]])

    print("avg", avg3)
    print("desvio1", desvio3)

    draw([avg1, desvio1], [avg2, desvio2], [avg3, desvio3])
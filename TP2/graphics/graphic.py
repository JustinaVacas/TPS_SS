import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


def draw(orders1, orders2, orders3, orders4, orders5, orders6):
    #plt.style.use('_mpl-gallery')

    # make data:
    x = [0.1, 1, 2, 3, 4, 5]
    y = [orders1[0], orders2[0], orders3[0], orders4[0], orders5[0], orders6[0]]
    yerr = [orders1[1], orders2[1], orders3[1], orders4[1], orders5[1], orders6[1]]

    # plot:
    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, fmt='o', linewidth=2, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2)

    ax.set(xlim=(0, 5.3), xticks=np.arange(0, 5.3, 1),
           ylim=(0, 1.1), yticks=np.arange(0, 1.1, 0.2))

    plt.xlabel('Ruido')
    plt.ylabel('Orden')


    plt.show()


def parseParameters(file1):

    with open(file1) as ordersFile:
        ordersLines = ordersFile.readlines()

    orders = []
    for line in ordersLines:
        newline = line.strip()
        orders.append(float(newline))

    avg = mean(orders[100:])
    desvio = np.std(orders[100:])
    print("avg ", avg)
    print("avg criollo ", sum(orders[100:])/len(orders[100:]))
    print("desvio ", desvio)
    return [avg, desvio]


if __name__ == '__main__':
    orders1 = parseParameters(sys.argv[1])
    orders2 = parseParameters(sys.argv[2])
    orders3 = parseParameters(sys.argv[3])
    orders4 = parseParameters(sys.argv[4])
    orders5 = parseParameters(sys.argv[5])
    orders6 = parseParameters(sys.argv[6])
    draw(orders1, orders2, orders3, orders4, orders5, orders6)



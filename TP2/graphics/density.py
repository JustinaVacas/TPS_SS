import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


def draw(orders1, orders2, orders3, orders4, orders5, orders6):
    #plt.style.use('_mpl-gallery')
    L=7
    print("orders1 ", orders1[0], orders1[1])
    # make data:
    x = [40/(L*L), 100/(L*L), 250/(L*L), 400/(L*L), 550/(L*L), 700/(L*L)]
    print("x ", x)
    y = [orders1[0], orders2[0], orders3[0], orders4[0], orders5[0], orders6[0]]
    print("y ", y)
    yerr = [orders1[1], orders2[1], orders3[1], orders4[1], orders5[1], orders6[1]]

    # plot:
    fig, ax = plt.subplots()

    ax.errorbar(x, y, yerr, linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2)

    ax.set(xlim=(0, 16), xticks=np.arange(0, 16, 1),
           ylim=(0, 1.1), yticks=np.arange(0, 1.1, 0.2))

    plt.xlabel('Densidad')
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
    return [avg, desvio]


if __name__ == '__main__':
    orders1 = parseParameters(sys.argv[1])
    orders2 = parseParameters(sys.argv[2])
    orders3 = parseParameters(sys.argv[3])
    orders4 = parseParameters(sys.argv[4])
    orders5 = parseParameters(sys.argv[5])
    orders6 = parseParameters(sys.argv[6])
    draw(orders1, orders2, orders3, orders4, orders5, orders6)



import matplotlib.pyplot as plt
import numpy as np
import sys
from statistics import mean


def draw(orders1, orders2, orders3, orders4, orders5, orders6, orders7, orders8, orders9, orders10, orders11, orders12, orders13, orders14, orders15, orders16, orders17, orders18):
    #plt.style.use('_mpl-gallery')
    L=7
    print("orders1 ", orders1[0], orders1[1])
    # make data:
    x = [40/(L*L), 100/(L*L), 250/(L*L), 400/(L*L), 550/(L*L), 700/(L*L)]

    y = [orders1[0], orders2[0], orders3[0], orders4[0], orders5[0], orders6[0]]
    y2= [orders7[0], orders8[0], orders9[0], orders10[0], orders11[0], orders12[0]]
    y3= [orders13[0], orders14[0], orders15[0], orders16[0], orders17[0], orders18[0]]

    yerr = [orders1[1], orders2[1], orders3[1], orders4[1], orders5[1], orders6[1]]
    yerr2 = [ orders7[1], orders8[1], orders9[1], orders10[1], orders11[1], orders12[1]]
    yerr3 = [ orders13[1], orders14[1], orders15[1], orders16[1], orders17[1], orders18[1]]

    # plot:
    fig, ax = plt.subplots()

    ax.errorbar(x, y2, yerr2, linewidth=1, capsize=10, color="orange", ecolor='lightblue', elinewidth=2, label='0.1')

    ax.set(xlim=(0, 16), xticks=np.arange(0, 16, 1),
           ylim=(0, 1.1), yticks=np.arange(0, 1.1, 0.2))

    ax.errorbar(x, y, yerr, linewidth=1, capsize=10, color="purple", ecolor='lightgreen', elinewidth=2, label='11')

    ax.set(xlim=(0, 16), xticks=np.arange(0, 16, 1),
       ylim=(0, 1.1), yticks=np.arange(0, 1.1, 0.2))

    ax.errorbar(x, y3, yerr3, linewidth=1, capsize=10, color="fuchsia", ecolor='pink', elinewidth=2, label='3')

    ax.set(xlim=(0, 16), xticks=np.arange(0, 16, 1),
           ylim=(0, 1.1), yticks=np.arange(0, 1.1, 0.2))

    plt.xlabel('Densidad')
    plt.ylabel('Orden')
    plt.legend(['1', '0.1','3'])


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
    orders7 = parseParameters(sys.argv[7])
    orders8 = parseParameters(sys.argv[8])
    orders9 = parseParameters(sys.argv[9])
    orders10 = parseParameters(sys.argv[10])
    orders11 = parseParameters(sys.argv[11])
    orders12 = parseParameters(sys.argv[12])
    orders13 = parseParameters(sys.argv[13])
    orders14 = parseParameters(sys.argv[14])
    orders15 = parseParameters(sys.argv[15])
    orders16 = parseParameters(sys.argv[16])
    orders17 = parseParameters(sys.argv[17])
    orders18 = parseParameters(sys.argv[18])

    draw(orders1, orders2, orders3, orders4, orders5, orders6, orders7, orders8, orders9, orders10, orders11, orders12, orders13, orders14, orders15, orders16, orders17, orders18)



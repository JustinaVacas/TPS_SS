import sys
import plotly.graph_objects as go
import matplotlib.pyplot as plt

import datetime


def draw(distance, t):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=t, y=distance,
        mode='lines',
        name='0.03',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo'),
            yaxis=dict(title='Distancia'),
            font=dict(size=20),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()

def drawAll(distance1, t1, distance2, t2):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=t1, y=distance1,
        mode='lines',
        name='22/09/2022',
        marker={'color':'fuchsia'}
    ))

    data.append(go.Scatter(
        x=t2, y=distance2,
        mode='lines',
        name='26/09/2022',
        marker={'color':'blue'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo'),
            yaxis=dict(title='Distancia'),
            legend=dict(title='Dia inicial'),
            font=dict(size=18),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def drawMin(distance, t):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=t, y=distance,
        mode='lines+markers',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Fecha inicial'),
            yaxis=dict(title='Distancia'),
            font=dict(size=20),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def parseParameters(file,initial_date):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    positionsV = []
    positionsN = []
    t = []
    dates = []
    count = 0
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 1:
            t.append(float(split[0]))
            date = initial_date + datetime.timedelta(seconds=float(split[0]))
            dates.append(date)
        elif len(split) == 5:
            if count == 1: #tierra es 1 - 2 es venus
                positionsV.append([float(split[1]), float(split[2])])
                count += 1
            elif count == 3:
                positionsN.append([float(split[1]), float(split[2])])
                count = 0
            else:
                count += 1

    distance = []
    min = 0
    xv = 0
    yv = 0
    xn = 0
    yn = 0
    aux = 0.0
    count = 0
    index = 0
    for n, v in zip(positionsN, positionsV):
        aux = ((n[0] - v[0])**2 + (n[1] - v[1])**2)**0.5 - 6051.84          # menos el radio de venus
        distance.append(aux)
        if (min > aux) & (min != 0):
            min = aux
            xv = v[0]
            yv = v[1]
            xn = n[0]
            yn = n[1]
            index = count
        if min == 0:
            min = aux
            index = count
        count += 1

    # print("distancia minima ", min)
    # print("fecha ", dates[index])
    # print("xv", xv)
    # print("yv", yv)
    # print("xn", xn)
    # print("yn", yn)
    # print("fechas", dates)
    return distance, t, dates, min, dates[index]


if __name__ == '__main__':

    distance24_03_23, t24_03_23, dates24_03_23, min24_03_23, fecha_min24_03_23 = parseParameters(sys.argv[1], datetime.date(2023, 3, 24))
    distance25_03_23, t25_03_23, dates25031_23, min25_03_23, fecha_min25_03_23 = parseParameters(sys.argv[2], datetime.date(2023, 3, 25))
    distance26_03_23, t26_03_23, dates26_03_23, min26_03_23, fecha_min26_03_23 = parseParameters(sys.argv[3], datetime.date(2023, 3, 26))
    distance27_03_23, t27_03_23, dates27_03_23, min27_03_23, fecha_min27_03_23 = parseParameters(sys.argv[4], datetime.date(2023, 3, 27))
    distance28_03_23, t28_03_23, dates28_00323, min28_03_23, fecha_min28_03_23 = parseParameters(sys.argv[5], datetime.date(2023, 3, 28))
    distance29_03_23, t29_03_23, dates29_03_23, min29_03_23, fecha_min29_03_23 = parseParameters(sys.argv[6], datetime.date(2023, 3, 29))
    distance30_03_23, t30_03_23, dates30_03_23, min30_03_23, fecha_min30_03_23 = parseParameters(sys.argv[7], datetime.date(2023, 3, 30))
    distance31_03_23, t31_03_23, dates31_03_23, min31_03_23, fecha_min31_03_23 = parseParameters(sys.argv[8], datetime.date(2023, 3, 31))
    #  abril
    distance1_04_23, t1_04_23, dates1_04_23, min1_04_23, fecha_min1_04_23 = parseParameters(sys.argv[9], datetime.date(2023, 4, 1))
    distance2_04_23, t2_04_23, dates2_04_23, min2_04_23, fecha_min2_04_23 = parseParameters(sys.argv[10], datetime.date(2023, 4, 2))
    distance3_04_23, t3_04_23, dates3_04_23, min3_04_23, fecha_min3_04_23 = parseParameters(sys.argv[11], datetime.date(2023, 4, 3))
    distance4_04_23, t4_04_23, dates4_04_23, min4_04_23, fecha_min4_04_23 = parseParameters(sys.argv[12], datetime.date(2023, 4, 4))
    distance5_04_23, t5_04_23, dates5_04_23, min5_04_23, fecha_min5_04_23 = parseParameters(sys.argv[13], datetime.date(2023, 4, 5))
    distance6_04_23, t6_04_23, dates6_04_23, min6_04_23, fecha_min6_04_23 = parseParameters(sys.argv[14], datetime.date(2023, 4, 6))
    distance7_04_23, t7_04_23, dates7_04_23, min7_04_23, fecha_min7_04_23 = parseParameters(sys.argv[15], datetime.date(2023, 4, 7))
    distance8_04_23, t8_04_23, dates8_04_23, min8_04_23, fecha_min8_04_23 = parseParameters(sys.argv[16], datetime.date(2023, 4, 8))
    distance9_04_23, t9_04_23, dates9_04_23, min9_04_23, fecha_min9_04_23 = parseParameters(sys.argv[17], datetime.date(2023, 4, 9))
    distance10_04_23, t10_04_23, dates10_04_23, min10_04_23, fecha_min10_04_23 = parseParameters(sys.argv[18], datetime.date(2023, 4, 10))
    distance11_04_23, t11_04_23, dates11_04_23, min11_04_23, fecha_min11_04_23 = parseParameters(sys.argv[19], datetime.date(2023, 4, 11))
    distance12_04_23, t12_04_23, dates12_04_23, min12_04_23, fecha_min12_04_23 = parseParameters(sys.argv[20], datetime.date(2023, 4, 12))
    distance13_04_23, t13_04_23, dates13_04_23, min13_04_23, fecha_min13_04_23 = parseParameters(sys.argv[21], datetime.date(2023, 4, 13))
    distance14_04_23, t14_04_23, dates14_04_23, min14_04_23, fecha_min14_04_23 = parseParameters(sys.argv[22], datetime.date(2023, 4, 14))
    distance15_04_23, t15_04_23, dates15_04_23, min15_04_23, fecha_min15_04_23 = parseParameters(sys.argv[23], datetime.date(2023, 4, 15))
    distance16_04_23, t16_04_23, dates16_04_23, min16_04_23, fecha_min16_04_23 = parseParameters(sys.argv[24], datetime.date(2023, 4, 16))
    distance17_04_23, t17_04_23, dates17_04_23, min17_04_23, fecha_min17_04_23 = parseParameters(sys.argv[25], datetime.date(2023, 4, 17))
    distance18_04_23, t18_04_23, dates18_04_23, min18_04_23, fecha_min18_04_23 = parseParameters(sys.argv[26], datetime.date(2023, 4, 18))
    distance19_04_23, t19_04_23, dates19_04_23, min19_04_23, fecha_min19_04_23 = parseParameters(sys.argv[27], datetime.date(2023, 4, 19))
    distance20_04_23, t20_04_23, dates20_04_23, min20_04_23, fecha_min20_04_23 = parseParameters(sys.argv[28], datetime.date(2023, 4, 20))
    distance21_04_23, t21_04_23, dates21_04_23, min21_04_23, fecha_min21_04_23 = parseParameters(sys.argv[29], datetime.date(2023, 4, 21))
    distance22_04_23, t22_04_23, dates22_04_23, min22_04_23, fecha_min22_04_23 = parseParameters(sys.argv[30], datetime.date(2023, 4, 22))
    distance23_04_23, t23_04_23, dates23_04_23, min23_04_23, fecha_min23_04_23 = parseParameters(sys.argv[31], datetime.date(2023, 4, 23))
    distance24_04_23, t24_04_23, dates24_04_23, min24_04_23, fecha_min24_04_23 = parseParameters(sys.argv[32], datetime.date(2023, 4, 24))
    distance25_04_23, t25_04_23, dates25_04_23, min25_04_23, fecha_min25_04_23 = parseParameters(sys.argv[33], datetime.date(2023, 4, 25))
    distance26_04_23, t26_04_23, dates26_04_23, min26_04_23, fecha_min26_04_23 = parseParameters(sys.argv[34], datetime.date(2023, 4, 26))
    distance27_04_23, t27_04_23, dates27_04_23, min27_04_23, fecha_min27_04_23 = parseParameters(sys.argv[35], datetime.date(2023, 4, 27))
    distance28_04_23, t28_04_23, dates28_04_23, min28_04_23, fecha_min28_04_23 = parseParameters(sys.argv[36], datetime.date(2023, 4, 28))
    distance29_04_23, t29_04_23, dates29_04_23, min29_04_23, fecha_min29_04_23 = parseParameters(sys.argv[37], datetime.date(2023, 4, 29))
    distance30_04_23, t30_04_23, dates30_04_23, min30_04_23, fecha_min30_04_23 = parseParameters(sys.argv[38], datetime.date(2023, 4, 30))
    #mayo
    distance1_05_23, t1_05_23, dates1_05_23, min1_05_23, fecha_min1_05_23 = parseParameters(sys.argv[39], datetime.date(2023, 5, 1))
    distance2_05_23, t2_05_23, dates2_05_23, min2_05_23, fecha_min2_05_23 = parseParameters(sys.argv[40], datetime.date(2023, 5, 2))
    distance3_05_23, t3_05_23, dates3_05_23, min3_05_23, fecha_min3_05_23 = parseParameters(sys.argv[41], datetime.date(2023, 5, 3))
    distance4_05_23, t4_05_23, dates4_05_23, min4_05_23, fecha_min4_05_23 = parseParameters(sys.argv[42], datetime.date(2023, 5, 4))
    distance5_05_23, t5_05_23, dates5_05_23, min5_05_23, fecha_min5_05_23 = parseParameters(sys.argv[43], datetime.date(2023, 5, 5))
    distance6_05_23, t6_05_23, dates6_05_23, min6_05_23, fecha_min6_05_23 = parseParameters(sys.argv[44], datetime.date(2023, 5, 6))
    distance7_05_23, t7_05_23, dates7_05_23, min7_05_23, fecha_min7_05_23 = parseParameters(sys.argv[45], datetime.date(2023, 5, 7))
    distance8_05_23, t8_05_23, dates8_05_23, min8_05_23, fecha_min8_05_23 = parseParameters(sys.argv[46], datetime.date(2023, 5, 8))
    distance9_05_23, t9_05_23, dates9_05_23, min9_05_23, fecha_min9_05_23 = parseParameters(sys.argv[47], datetime.date(2023, 5, 9))
    distance10_05_23, t10_05_23, dates10_05_23, min10_05_23, fecha_min10_05_23 = parseParameters(sys.argv[48], datetime.date(2023, 5, 10))
    distance11_05_23, t11_05_23, dates11_05_23, min11_05_23, fecha_min11_05_23 = parseParameters(sys.argv[49], datetime.date(2023, 5, 11))
    distance12_05_23, t12_05_23, dates12_05_23, min12_05_23, fecha_min12_05_23 = parseParameters(sys.argv[50], datetime.date(2023, 5, 12))
    distance13_05_23, t13_05_23, dates13_05_23, min13_05_23, fecha_min13_05_23 = parseParameters(sys.argv[51], datetime.date(2023, 5, 13))
    distance14_05_23, t14_05_23, dates14_05_23, min14_05_23, fecha_min14_05_23 = parseParameters(sys.argv[52], datetime.date(2023, 5, 14))
    distance15_05_23, t15_05_23, dates15_05_23, min15_05_23, fecha_min15_05_23 = parseParameters(sys.argv[53], datetime.date(2023, 5, 15))
    distance16_05_23, t16_05_23, dates16_05_23, min16_05_23, fecha_min16_05_23 = parseParameters(sys.argv[54], datetime.date(2023, 5, 16))
    distance17_05_23, t17_05_23, dates17_05_23, min17_05_23, fecha_min17_05_23 = parseParameters(sys.argv[55], datetime.date(2023, 5, 17))
    distance18_05_23, t18_05_23, dates18_05_23, min18_05_23, fecha_min18_05_23 = parseParameters(sys.argv[56], datetime.date(2023, 5, 18))
    distance19_05_23, t19_05_23, dates19_05_23, min19_05_23, fecha_min19_05_23 = parseParameters(sys.argv[57], datetime.date(2023, 5, 19))
    distance20_05_23, t20_05_23, dates20_05_23, min20_05_23, fecha_min20_05_23 = parseParameters(sys.argv[58], datetime.date(2023, 5, 20))
    distance21_05_23, t21_05_23, dates21_05_23, min21_05_23, fecha_min21_05_23 = parseParameters(sys.argv[59], datetime.date(2023, 5, 21))
    distance22_05_23, t22_05_23, dates22_05_23, min22_05_23, fecha_min22_05_23 = parseParameters(sys.argv[60], datetime.date(2023, 5, 22))
    distance23_05_23, t23_05_23, dates23_05_23, min23_05_23, fecha_min23_05_23 = parseParameters(sys.argv[61], datetime.date(2023, 5, 23))
    distance24_05_23, t24_05_23, dates24_05_23, min24_05_23, fecha_min24_05_23 = parseParameters(sys.argv[62], datetime.date(2023, 5, 24))
    distance25_05_23, t25_05_23, dates25_05_23, min25_05_23, fecha_min25_05_23 = parseParameters(sys.argv[63], datetime.date(2023, 5, 25))
    distance26_05_23, t26_05_23, dates26_05_23, min26_05_23, fecha_min26_05_23 = parseParameters(sys.argv[64], datetime.date(2023, 5, 26))
    distance27_05_23, t27_05_23, dates27_05_23, min27_05_23, fecha_min27_05_23 = parseParameters(sys.argv[65], datetime.date(2023, 5, 27))
    distance28_05_23, t28_05_23, dates28_05_23, min28_05_23, fecha_min28_05_23 = parseParameters(sys.argv[66], datetime.date(2023, 5, 28))
    distance29_05_23, t29_05_23, dates29_05_23, min29_05_23, fecha_min29_05_23 = parseParameters(sys.argv[67], datetime.date(2023, 5, 29))
    distance30_05_23, t30_05_23, dates30_05_23, min30_05_23, fecha_min30_05_23 = parseParameters(sys.argv[68], datetime.date(2023, 5, 30))
    distance31_05_23, t31_05_23, dates31_05_23, min31_05_23, fecha_min31_05_23 = parseParameters(sys.argv[69], datetime.date(2023, 5, 31))
    #junio
    distance1_06_23, t1_06_23, dates1_06_23, min1_06_23, fecha_min1_06_23 = parseParameters(sys.argv[70], datetime.date(2023, 6, 1))
    distance2_06_23, t2_06_23, dates2_06_23, min2_06_23, fecha_min2_06_23 = parseParameters(sys.argv[71], datetime.date(2023, 6, 2))
    distance3_06_23, t3_06_23, dates3_06_23, min3_06_23, fecha_min3_06_23 = parseParameters(sys.argv[72], datetime.date(2023, 6, 3))
    distance4_06_23, t4_06_23, dates4_06_23, min4_06_23, fecha_min4_06_23 = parseParameters(sys.argv[73], datetime.date(2023, 6, 4))
    distance5_06_23, t5_06_23, dates5_06_23, min5_06_23, fecha_min5_06_23 = parseParameters(sys.argv[74], datetime.date(2023, 6, 5))
    distance6_06_23, t6_06_23, dates6_06_23, min6_06_23, fecha_min6_06_23 = parseParameters(sys.argv[75], datetime.date(2023, 6, 6))
    distance7_06_23, t7_06_23, dates7_06_23, min7_06_23, fecha_min7_06_23 = parseParameters(sys.argv[76], datetime.date(2023, 6, 7))
    distance8_06_23, t8_06_23, dates8_06_23, min8_06_23, fecha_min8_06_23 = parseParameters(sys.argv[77], datetime.date(2023, 6, 8))
    distance9_06_23, t9_06_23, dates9_06_23, min9_06_23, fecha_min9_06_23 = parseParameters(sys.argv[78], datetime.date(2023, 6, 9))
    distance10_06_23, t10_06_23, dates10_06_23, min10_06_23, fecha_min10_06_23 = parseParameters(sys.argv[79], datetime.date(2023, 6, 10))
    distance11_06_23, t11_06_23, dates11_06_23, min11_06_23, fecha_min11_06_23 = parseParameters(sys.argv[80], datetime.date(2023, 6, 11))
    distance12_06_23, t12_06_23, dates12_06_23, min12_06_23, fecha_min12_06_23 = parseParameters(sys.argv[81], datetime.date(2023, 6, 12))
    distance13_06_23, t13_06_23, dates13_06_23, min13_06_23, fecha_min13_06_23 = parseParameters(sys.argv[82], datetime.date(2023, 6, 13))
    distance14_06_23, t14_06_23, dates14_06_23, min14_06_23, fecha_min14_06_23 = parseParameters(sys.argv[83], datetime.date(2023, 6, 14))
    distance15_06_23, t15_06_23, dates15_06_23, min15_06_23, fecha_min15_06_23 = parseParameters(sys.argv[84], datetime.date(2023, 6, 15))
    distance16_06_23, t16_06_23, dates16_06_23, min16_06_23, fecha_min16_06_23 = parseParameters(sys.argv[85], datetime.date(2023, 6, 16))
    distance17_06_23, t17_06_23, dates17_06_23, min17_06_23, fecha_min17_06_23 = parseParameters(sys.argv[86], datetime.date(2023, 6, 17))
    distance18_06_23, t18_06_23, dates18_06_23, min18_06_23, fecha_min18_06_23 = parseParameters(sys.argv[87], datetime.date(2023, 6, 18))
    distance19_06_23, t19_06_23, dates19_06_23, min19_06_23, fecha_min19_06_23 = parseParameters(sys.argv[88], datetime.date(2023, 6, 19))
    distance20_06_23, t20_06_23, dates20_06_23, min20_06_23, fecha_min20_06_23 = parseParameters(sys.argv[89], datetime.date(2023, 6, 20))
    distance21_06_23, t21_06_23, dates21_06_23, min21_06_23, fecha_min21_06_23 = parseParameters(sys.argv[90], datetime.date(2023, 6, 21))
    distance22_06_23, t22_06_23, dates22_06_23, min22_06_23, fecha_min22_06_23 = parseParameters(sys.argv[91], datetime.date(2023, 6, 22))
    distance23_06_23, t23_06_23, dates23_06_23, min23_06_23, fecha_min23_06_23 = parseParameters(sys.argv[92], datetime.date(2023, 6, 23))
    distance24_06_23, t24_06_23, dates24_06_23, min24_06_23, fecha_min24_06_23 = parseParameters(sys.argv[93], datetime.date(2023, 6, 24))
    distance25_06_23, t25_06_23, dates25_06_23, min25_06_23, fecha_min25_06_23 = parseParameters(sys.argv[94], datetime.date(2023, 6, 25))
    distance26_06_23, t26_06_23, dates26_06_23, min26_06_23, fecha_min26_06_23 = parseParameters(sys.argv[95], datetime.date(2023, 6, 26))
    distance27_06_23, t27_06_23, dates27_06_23, min27_06_23, fecha_min27_06_23 = parseParameters(sys.argv[96], datetime.date(2023, 6, 27))
    distance28_06_23, t28_06_23, dates28_06_23, min28_06_23, fecha_min28_06_23 = parseParameters(sys.argv[97], datetime.date(2023, 6, 28))
    distance29_06_23, t29_06_23, dates29_06_23, min29_06_23, fecha_min29_06_23 = parseParameters(sys.argv[98], datetime.date(2023, 6, 29))
    distance30_06_23, t30_06_23, dates30_06_23, min30_06_23, fecha_min30_06_23 = parseParameters(sys.argv[99], datetime.date(2023, 6, 30))
    #julio
    distance1_07_23, t1_07_23, dates1_07_23, min1_07_23, fecha_min1_07_23 = parseParameters(sys.argv[100], datetime.date(2023, 7, 1))
    distance2_07_23, t2_07_23, dates2_07_23, min2_07_23, fecha_min2_07_23 = parseParameters(sys.argv[101], datetime.date(2023, 7, 2))
    distance3_07_23, t3_07_23, dates3_07_23, min3_07_23, fecha_min3_07_23 = parseParameters(sys.argv[102], datetime.date(2023, 7, 3))
    distance4_07_23, t4_07_23, dates4_07_23, min4_07_23, fecha_min4_07_23 = parseParameters(sys.argv[103], datetime.date(2023, 7, 4))
    distance5_07_23, t5_07_23, dates5_07_23, min5_07_23, fecha_min5_07_23 = parseParameters(sys.argv[104], datetime.date(2023, 7, 5))
    distance6_07_23, t6_07_23, dates6_07_23, min6_07_23, fecha_min6_07_23 = parseParameters(sys.argv[105], datetime.date(2023, 7, 6))
    distance7_07_23, t7_07_23, dates7_07_23, min7_07_23, fecha_min7_07_23 = parseParameters(sys.argv[106], datetime.date(2023, 7, 7))
    distance8_07_23, t8_07_23, dates8_07_23, min8_07_23, fecha_min8_07_23 = parseParameters(sys.argv[107], datetime.date(2023, 7, 8))
    distance9_07_23, t9_07_23, dates9_07_23, min9_07_23, fecha_min9_07_23 = parseParameters(sys.argv[108], datetime.date(2023, 7, 9))
    distance10_07_23, t10_07_23, dates10_07_23, min10_07_23, fecha_min10_07_23 = parseParameters(sys.argv[109], datetime.date(2023, 7, 10))
    distance11_07_23, t11_07_23, dates11_07_23, min11_07_23, fecha_min11_07_23 = parseParameters(sys.argv[110], datetime.date(2023, 7, 11))
    distance12_07_23, t12_07_23, dates12_07_23, min12_07_23, fecha_min12_07_23 = parseParameters(sys.argv[111], datetime.date(2023, 7, 12))
    distance13_07_23, t13_07_23, dates13_07_23, min13_07_23, fecha_min13_07_23 = parseParameters(sys.argv[112], datetime.date(2023, 7, 13))
    distance14_07_23, t14_07_23, dates14_07_23, min14_07_23, fecha_min14_07_23 = parseParameters(sys.argv[113], datetime.date(2023, 7, 14))
    distance15_07_23, t15_07_23, dates15_07_23, min15_07_23, fecha_min15_07_23 = parseParameters(sys.argv[114], datetime.date(2023, 7, 15))
    distance16_07_23, t16_07_23, dates16_07_23, min16_07_23, fecha_min16_07_23 = parseParameters(sys.argv[115], datetime.date(2023, 7, 16))
    distance17_07_23, t17_07_23, dates17_07_23, min17_07_23, fecha_min17_07_23 = parseParameters(sys.argv[116], datetime.date(2023, 7, 17))
    distance18_07_23, t18_07_23, dates18_07_23, min18_07_23, fecha_min18_07_23 = parseParameters(sys.argv[117], datetime.date(2023, 7, 18))
    distance19_07_23, t19_07_23, dates19_07_23, min19_07_23, fecha_min19_07_23 = parseParameters(sys.argv[118], datetime.date(2023, 7, 19))
    distance20_07_23, t20_07_23, dates20_07_23, min20_07_23, fecha_min20_07_23 = parseParameters(sys.argv[119], datetime.date(2023, 7, 20))
    distance21_07_23, t21_07_23, dates21_07_23, min21_07_23, fecha_min21_07_23 = parseParameters(sys.argv[120], datetime.date(2023, 7, 21))
    distance22_07_23, t22_07_23, dates22_07_23, min22_07_23, fecha_min22_07_23 = parseParameters(sys.argv[121], datetime.date(2023, 7, 22))
    distance23_07_23, t23_07_23, dates23_07_23, min23_07_23, fecha_min23_07_23 = parseParameters(sys.argv[122], datetime.date(2023, 7, 23))
    distance24_07_23, t24_07_23, dates24_07_23, min24_07_23, fecha_min24_07_23 = parseParameters(sys.argv[123], datetime.date(2023, 7, 24))
    distance25_07_23, t25_07_23, dates25_07_23, min25_07_23, fecha_min25_07_23 = parseParameters(sys.argv[124], datetime.date(2023, 7, 25))
    distance26_07_23, t26_07_23, dates26_07_23, min26_07_23, fecha_min26_07_23 = parseParameters(sys.argv[125], datetime.date(2023, 7, 26))
    distance27_07_23, t27_07_23, dates27_07_23, min27_07_23, fecha_min27_07_23 = parseParameters(sys.argv[126], datetime.date(2023, 7, 27))
    distance28_07_23, t28_07_23, dates28_07_23, min28_07_23, fecha_min28_07_23 = parseParameters(sys.argv[127], datetime.date(2023, 7, 28))
    distance29_07_23, t29_07_23, dates29_07_23, min29_07_23, fecha_min29_07_23 = parseParameters(sys.argv[128], datetime.date(2023, 7, 29))
    distance30_07_23, t30_07_23, dates30_07_23, min30_07_23, fecha_min30_07_23 = parseParameters(sys.argv[129], datetime.date(2023, 7, 30))
    distance31_07_23, t31_07_23, dates31_07_23, min31_07_23, fecha_min31_07_23 = parseParameters(sys.argv[130], datetime.date(2023, 7, 31))
    #agosto
    distance1_08_23, t1_08_23, dates1_08_23, min1_08_23, fecha_min1_08_23 = parseParameters(sys.argv[131], datetime.date(2023, 8, 1))
    distance2_08_23, t2_08_23, dates2_08_23, min2_08_23, fecha_min2_08_23 = parseParameters(sys.argv[132], datetime.date(2023, 8, 2))
    distance3_08_23, t3_08_23, dates3_08_23, min3_08_23, fecha_min3_08_23 = parseParameters(sys.argv[133], datetime.date(2023, 8, 3))
    distance4_08_23, t4_08_23, dates4_08_23, min4_08_23, fecha_min4_08_23 = parseParameters(sys.argv[134], datetime.date(2023, 8, 4))
    distance5_08_23, t5_08_23, dates5_08_23, min5_08_23, fecha_min5_08_23 = parseParameters(sys.argv[135], datetime.date(2023, 8, 5))
    distance6_08_23, t6_08_23, dates6_08_23, min6_08_23, fecha_min6_08_23 = parseParameters(sys.argv[136], datetime.date(2023, 8, 6))
    distance7_08_23, t7_08_23, dates7_08_23, min7_08_23, fecha_min7_08_23 = parseParameters(sys.argv[137], datetime.date(2023, 8, 7))
    distance8_08_23, t8_08_23, dates8_08_23, min8_08_23, fecha_min8_08_23 = parseParameters(sys.argv[138], datetime.date(2023, 8, 8))
    distance9_08_23, t9_08_23, dates9_08_23, min9_08_23, fecha_min9_08_23 = parseParameters(sys.argv[139], datetime.date(2023, 8, 9))
    distance10_08_23, t10_08_23, dates10_08_23, min10_08_23, fecha_min10_08_23 = parseParameters(sys.argv[140], datetime.date(2023, 8, 10))
    distance11_08_23, t11_08_23, dates11_08_23, min11_08_23, fecha_min11_08_23 = parseParameters(sys.argv[141], datetime.date(2023, 8, 11))
    distance12_08_23, t12_08_23, dates12_08_23, min12_08_23, fecha_min12_08_23 = parseParameters(sys.argv[142], datetime.date(2023, 8, 12))
    distance13_08_23, t13_08_23, dates13_08_23, min13_08_23, fecha_min13_08_23 = parseParameters(sys.argv[143], datetime.date(2023, 8, 13))
    distance14_08_23, t14_08_23, dates14_08_23, min14_08_23, fecha_min14_08_23 = parseParameters(sys.argv[144], datetime.date(2023, 8, 14))
    distance15_08_23, t15_08_23, dates15_08_23, min15_08_23, fecha_min15_08_23 = parseParameters(sys.argv[145], datetime.date(2023, 8, 15))
    distance16_08_23, t16_08_23, dates16_08_23, min16_08_23, fecha_min16_08_23 = parseParameters(sys.argv[146], datetime.date(2023, 8, 16))
    distance17_08_23, t17_08_23, dates17_08_23, min17_08_23, fecha_min17_08_23 = parseParameters(sys.argv[147], datetime.date(2023, 8, 17))
    distance18_08_23, t18_08_23, dates18_08_23, min18_08_23, fecha_min18_08_23 = parseParameters(sys.argv[148], datetime.date(2023, 8, 18))
    distance19_08_23, t19_08_23, dates19_08_23, min19_08_23, fecha_min19_08_23 = parseParameters(sys.argv[149], datetime.date(2023, 8, 19))
    distance20_08_23, t20_08_23, dates20_08_23, min20_08_23, fecha_min20_08_23 = parseParameters(sys.argv[150], datetime.date(2023, 8, 20))
    distance21_08_23, t21_08_23, dates21_08_23, min21_08_23, fecha_min21_08_23 = parseParameters(sys.argv[151], datetime.date(2023, 8, 21))
    distance22_08_23, t22_08_23, dates22_08_23, min22_08_23, fecha_min22_08_23 = parseParameters(sys.argv[152], datetime.date(2023, 8, 22))
    distance23_08_23, t23_08_23, dates23_08_23, min23_08_23, fecha_min23_08_23 = parseParameters(sys.argv[153], datetime.date(2023, 8, 23))
    distance24_08_23, t24_08_23, dates24_08_23, min24_08_23, fecha_min24_08_23 = parseParameters(sys.argv[154], datetime.date(2023, 8, 24))
    distance25_08_23, t25_08_23, dates25_08_23, min25_08_23, fecha_min25_08_23 = parseParameters(sys.argv[155], datetime.date(2023, 8, 25))
    distance26_08_23, t26_08_23, dates26_08_23, min26_08_23, fecha_min26_08_23 = parseParameters(sys.argv[156], datetime.date(2023, 8, 26))
    distance27_08_23, t27_08_23, dates27_08_23, min27_08_23, fecha_min27_08_23 = parseParameters(sys.argv[157], datetime.date(2023, 8, 27))
    distance28_08_23, t28_08_23, dates28_08_23, min28_08_23, fecha_min28_08_23 = parseParameters(sys.argv[158], datetime.date(2023, 8, 28))
    distance29_08_23, t29_08_23, dates29_08_23, min29_08_23, fecha_min29_08_23 = parseParameters(sys.argv[159], datetime.date(2023, 8, 29))
    distance30_08_23, t30_08_23, dates30_08_23, min30_08_23, fecha_min30_08_23 = parseParameters(sys.argv[160], datetime.date(2023, 8, 30))
    distance31_08_23, t31_08_23, dates31_08_23, min31_08_23, fecha_min31_08_23 = parseParameters(sys.argv[161], datetime.date(2023, 8, 31))
    #septiembre
    distance1_09_23, t1_09_23, dates1_09_23, min1_09_23, fecha_min1_09_23 = parseParameters(sys.argv[162], datetime.date(2023, 9, 1))
    distance2_09_23, t2_09_23, dates2_09_23, min2_09_23, fecha_min2_09_23 = parseParameters(sys.argv[163], datetime.date(2023, 9, 2))
    distance3_09_23, t3_09_23, dates3_09_23, min3_09_23, fecha_min3_09_23 = parseParameters(sys.argv[164], datetime.date(2023, 9, 3))
    distance4_09_23, t4_09_23, dates4_09_23, min4_09_23, fecha_min4_09_23 = parseParameters(sys.argv[165], datetime.date(2023, 9, 4))
    distance5_09_23, t5_09_23, dates5_09_23, min5_09_23, fecha_min5_09_23 = parseParameters(sys.argv[166], datetime.date(2023, 9, 5))
    distance6_09_23, t6_09_23, dates6_09_23, min6_09_23, fecha_min6_09_23 = parseParameters(sys.argv[167], datetime.date(2023, 9, 6))
    distance7_09_23, t7_09_23, dates7_09_23, min7_09_23, fecha_min7_09_23 = parseParameters(sys.argv[168], datetime.date(2023, 9, 7))
    distance8_09_23, t8_09_23, dates8_09_23, min8_09_23, fecha_min8_09_23 = parseParameters(sys.argv[168], datetime.date(2023, 9, 8))
    distance9_09_23, t9_09_23, dates9_09_23, min9_09_23, fecha_min9_09_23 = parseParameters(sys.argv[170], datetime.date(2023, 9, 9))
    distance10_09_23, t10_09_23, dates10_09_23, min10_09_23, fecha_min10_09_23 = parseParameters(sys.argv[171], datetime.date(2023, 9, 10))
    distance11_09_23, t11_09_23, dates11_09_23, min11_09_23, fecha_min11_09_23 = parseParameters(sys.argv[172], datetime.date(2023, 9, 11))
    distance12_09_23, t12_09_23, dates12_09_23, min12_09_23, fecha_min12_09_23 = parseParameters(sys.argv[173], datetime.date(2023, 9, 12))
    distance13_09_23, t13_09_23, dates13_09_23, min13_09_23, fecha_min13_09_23 = parseParameters(sys.argv[174], datetime.date(2023, 9, 13))
    distance14_09_23, t14_09_23, dates14_09_23, min14_09_23, fecha_min14_09_23 = parseParameters(sys.argv[175], datetime.date(2023, 9, 14))
    distance15_09_23, t15_09_23, dates15_09_23, min15_09_23, fecha_min15_09_23 = parseParameters(sys.argv[176], datetime.date(2023, 9, 15))
    distance16_09_23, t16_09_23, dates16_09_23, min16_09_23, fecha_min16_09_23 = parseParameters(sys.argv[177], datetime.date(2023, 9, 16))
    distance17_09_23, t17_09_23, dates17_09_23, min17_09_23, fecha_min17_09_23 = parseParameters(sys.argv[178], datetime.date(2023, 9, 17))
    distance18_09_23, t18_09_23, dates18_09_23, min18_09_23, fecha_min18_09_23 = parseParameters(sys.argv[179], datetime.date(2023, 9, 18))
    distance19_09_23, t19_09_23, dates19_09_23, min19_09_23, fecha_min19_09_23 = parseParameters(sys.argv[180], datetime.date(2023, 9, 19))
    distance20_09_23, t20_09_23, dates20_09_23, min20_09_23, fecha_min20_09_23 = parseParameters(sys.argv[181], datetime.date(2023, 9, 20))
    distance21_09_23, t21_09_23, dates21_09_23, min21_09_23, fecha_min21_09_23 = parseParameters(sys.argv[182], datetime.date(2023, 9, 21))
    distance22_09_23, t22_09_23, dates22_09_23, min22_09_23, fecha_min22_09_23 = parseParameters(sys.argv[183], datetime.date(2023, 9, 22))
    distance23_09_23, t23_09_23, dates23_09_23, min23_09_23, fecha_min23_09_23 = parseParameters(sys.argv[184], datetime.date(2023, 9, 23))
    # draw(distance23, dates23)
    # draw(distance30, dates30)

    mins = [min24_03_23,min25_03_23,min26_03_23,min27_03_23,min28_03_23,min29_03_23,min30_03_23,min31_03_23,min1_04_23,min2_04_23,min3_04_23,min4_04_23,min5_04_23,min6_04_23,min7_04_23,min8_04_23,min9_04_23,min10_04_23,min11_04_23,min12_04_23,min13_04_23,min14_04_23,min15_04_23,min16_04_23,min17_04_23,min18_04_23,min19_04_23,min20_04_23,min21_04_23,min22_04_23,min23_04_23,
            min24_04_23,min25_04_23,min26_04_23,min27_04_23,min28_04_23,min29_04_23,min30_04_23,min1_05_23,min2_05_23,min3_05_23,min4_05_23,min5_05_23,min6_05_23,min7_05_23,min8_05_23,min9_05_23,min10_05_23,min11_05_23,min12_05_23,min13_05_23,min14_05_23,min15_05_23,min16_05_23,min17_05_23,min18_05_23,min19_05_23,min20_05_23,min21_05_23,min22_05_23,min23_05_23,
            min24_05_23,min25_05_23,min26_05_23,min27_05_23,min28_05_23,min29_05_23,min30_05_23,min31_05_23,min1_06_23,min2_06_23,min3_06_23,min4_06_23,min5_06_23,min6_06_23,min7_06_23,min8_06_23,min9_06_23,min10_06_23,min11_06_23,min12_06_23,min13_06_23,min14_06_23,min15_06_23,min16_06_23,min17_06_23,min18_06_23,min19_06_23,min20_06_23,min21_06_23,min22_06_23,min23_06_23,
            min24_06_23,min25_06_23,min26_06_23,min27_06_23,min28_06_23,min29_06_23,min30_06_23,min1_07_23,min2_07_23,min3_07_23,min4_07_23,min5_07_23,min6_07_23,min7_07_23,min8_07_23,min9_07_23,min10_07_23,min11_07_23,min12_07_23,min13_07_23,min14_07_23,min15_07_23,min16_07_23,min17_07_23,min18_07_23,min19_07_23,min20_07_23,min21_07_23,min22_07_23,min23_07_23,
            min24_07_23,min25_07_23,min26_07_23,min27_07_23,min28_07_23,min29_07_23,min30_07_23,min31_07_23,min1_08_23,min2_08_23,min3_08_23,min4_08_23,min5_08_23,min6_08_23,min7_08_23,min8_08_23,min9_08_23,min10_08_23,min11_08_23,min12_08_23,min13_08_23,min14_08_23,min15_08_23,min16_08_23,min17_08_23,min18_08_23,min19_08_23,min20_08_23,min21_08_23,min22_08_23,min23_08_23,
            min24_08_23,min25_08_23,min26_08_23,min27_08_23,min28_08_23,min29_08_23,min30_08_23,min31_08_23,min1_09_23,min2_09_23,min3_09_23,min4_09_23,min5_09_23,min6_09_23,min7_09_23,min8_09_23,min9_09_23,min10_09_23,min11_09_23,min12_09_23,min13_09_23,min14_09_23,min15_09_23,min16_09_23,min17_09_23,min18_09_23,min19_09_23,min20_09_23,min21_09_23,min22_09_23,min23_09_23
            ]
    #marzo
    '''
    min1_03_23,min2_03_23,min2_03_23,min4_03_23,min5_03_23,min6_03_23,min7_03_23,min8_03_23,min9_03_23,min10_03_23,min11_03_23,min12_03_23,min13_03_23,min14_03_23,min15_03_23,min16_03_23,min17_03_23,min18_03_23,min19_03_23,min20_03_23,min21_03_23,min22_03_23,min23_03_23,
    min24_03_23,min25_03_23,min26_03_23,min27_03_23,min28_03_23,min29_03_23,min30_03_23,min31_03_23
    '''
    #abril
    '''
    min1_04_23,min2_04_23,min2_04_23,min4_04_23,min5_04_23,min6_04_23,min7_04_23,min8_04_23,min9_04_23,min10_04_23,min11_04_23,min12_04_23,min13_04_23,min14_04_23,min15_04_23,min16_04_23,min17_04_23,min18_04_23,min19_04_23,min20_04_23,min21_04_23,min22_04_23,min23_04_23,
    min24_04_23,min25_04_23,min26_04_23,min27_04_23,min28_04_23,min29_04_23,min30_04_23
    '''
    #mayo
    '''
    min1_05_23,min2_05_23,min2_05_23,min4_05_23,min5_05_23,min6_05_23,min7_05_23,min8_05_23,min9_05_23,min10_05_23,min11_05_23,min12_05_23,min13_05_23,min14_05_23,min15_05_23,min16_05_23,min17_05_23,min18_05_23,min19_05_23,min20_05_23,min21_05_23,min22_05_23,min23_05_23,
    min24_05_23,min25_05_23,min26_05_23,min27_05_23,min28_05_23,min29_05_23,min30_05_23,min31_05_23
    '''
    #junio
    '''
    min1_06_23,min2_06_23,min2_06_23,min4_06_23,min5_06_23,min6_06_23,min7_06_23,min8_06_23,min9_06_23,min10_06_23,min11_06_23,min12_06_23,min13_06_23,min14_06_23,min15_06_23,min16_06_23,min17_06_23,min18_06_23,min19_06_23,min20_06_23,min21_06_23,min22_06_23,min23_06_23,
    min24_06_23,min25_06_23,min26_06_23,min27_06_23,min28_06_23,min29_06_23,min30_06_23
    '''
    #julio
    '''
    min1_07_23,min2_07_23,min2_07_23,min4_07_23,min5_07_23,min6_07_23,min7_07_23,min8_07_23,min9_07_23,min10_07_23,min11_07_23,min12_07_23,min13_07_23,min14_07_23,min15_07_23,min16_07_23,min17_07_23,min18_07_23,min19_07_23,min20_07_23,min21_07_23,min22_07_23,min23_07_23,
    min24_07_23,min25_07_23,min26_07_23,min27_07_23,min28_07_23,min29_07_23,min30_07_23,min31_07_23
    '''
    #agosto
    '''
    min1_08_23,min2_08_23,min2_08_23,min4_08_23,min5_08_23,min6_08_23,min7_08_23,min8_08_23,min9_08_23,min10_08_23,min11_08_23,min12_08_23,min13_08_23,min14_08_23,min15_08_23,min16_08_23,min17_08_23,min18_08_23,min19_08_23,min20_08_23,min21_08_23,min22_08_23,min23_08_23,
    min24_08_23,min25_08_23,min26_08_23,min27_08_23,min28_08_23,min29_08_23,min30_08_23,min31_08_23
    '''
    #septiembre
    '''
    min1_09_23,min2_09_23,min2_09_23,min4_09_23,min5_09_23,min6_09_23,min7_09_23,min8_09_23,min9_09_23,min10_09_23,min11_09_23,min12_09_23,min13_09_23,min14_09_23,min15_09_23,min16_09_23,min17_09_23,min18_09_23,min19_09_23,min20_09_23,min21_09_23,min22_09_23,min23_09_23
    '''
    fechas = [
        datetime.date(2023, 3, 24), datetime.date(2023, 3, 25), datetime.date(2023, 3, 26), datetime.date(2023, 3, 27), datetime.date(2023, 3, 28), datetime.date(2023, 3, 29), datetime.date(2023, 3, 30), datetime.date(2023, 3, 31),
        datetime.date(2023, 4, 1), datetime.date(2023, 4, 2), datetime.date(2023, 4, 3), datetime.date(2023, 4, 4), datetime.date(2023, 4, 5), datetime.date(2023, 4, 6), datetime.date(2023, 4, 7), datetime.date(2023, 4, 8),
        datetime.date(2023, 4, 9), datetime.date(2023, 4, 10), datetime.date(2023, 4, 11), datetime.date(2023, 4, 12), datetime.date(2023, 4, 13), datetime.date(2023, 4, 14), datetime.date(2023, 4, 15), datetime.date(2023, 4, 16), datetime.date(2023, 4, 17), datetime.date(2023, 4, 18), datetime.date(2023, 4, 19), datetime.date(2023, 4, 20), datetime.date(2023, 4, 21), datetime.date(2023, 4, 22),
        datetime.date(2023, 4, 23), datetime.date(2023, 4, 24), datetime.date(2023, 4, 25), datetime.date(2023, 4, 26), datetime.date(2023, 4, 27), datetime.date(2023, 4, 28), datetime.date(2023, 4, 29), datetime.date(2023, 4, 30), datetime.date(2023, 5, 1), datetime.date(2023, 5, 2), datetime.date(2023, 5, 3), datetime.date(2023, 5, 4), datetime.date(2023, 5, 5), datetime.date(2023, 5, 6), datetime.date(2023, 5, 7), datetime.date(2023, 5, 8),
        datetime.date(2023, 5, 9), datetime.date(2023, 5, 10), datetime.date(2023, 5, 11), datetime.date(2023, 5, 12), datetime.date(2023, 5, 13), datetime.date(2023, 5, 14), datetime.date(2023, 5, 15), datetime.date(2023, 5, 16), datetime.date(2023, 5, 17), datetime.date(2023, 5, 18), datetime.date(2023, 5, 19), datetime.date(2023, 5, 20), datetime.date(2023, 5, 21), datetime.date(2023, 5, 22),
        datetime.date(2023, 5, 23), datetime.date(2023, 5, 24), datetime.date(2023, 5, 25), datetime.date(2023, 5, 26), datetime.date(2023, 5, 27), datetime.date(2023, 5, 28), datetime.date(2023, 5, 29), datetime.date(2023, 5, 30), datetime.date(2023, 5, 31), datetime.date(2023, 6, 1), datetime.date(2023, 6, 2), datetime.date(2023, 6, 3), datetime.date(2023, 6, 4), datetime.date(2023, 6, 5), datetime.date(2023, 6, 6), datetime.date(2023, 6, 7), datetime.date(2023, 6, 8),
        datetime.date(2023, 6, 9), datetime.date(2023, 6, 10), datetime.date(2023, 6, 11), datetime.date(2023, 6, 12), datetime.date(2023, 6, 13), datetime.date(2023, 6, 14), datetime.date(2023, 6, 15), datetime.date(2023, 6, 16), datetime.date(2023, 6, 17), datetime.date(2023, 6, 18), datetime.date(2023, 6, 19), datetime.date(2023, 6, 20), datetime.date(2023, 6, 21), datetime.date(2023, 6, 22),
        datetime.date(2023, 6, 23), datetime.date(2023, 6, 24), datetime.date(2023, 6, 25), datetime.date(2023, 6, 26), datetime.date(2023, 6, 27), datetime.date(2023, 6, 28), datetime.date(2023, 6, 29), datetime.date(2023, 6, 30), datetime.date(2023, 7, 1), datetime.date(2023, 7, 2), datetime.date(2023, 7, 3), datetime.date(2023, 7, 4), datetime.date(2023, 7, 5), datetime.date(2023, 7, 6), datetime.date(2023, 7, 7), datetime.date(2023, 7, 8),
        datetime.date(2023, 7, 9), datetime.date(2023, 7, 10), datetime.date(2023, 7, 11), datetime.date(2023, 7, 12), datetime.date(2023, 7, 13), datetime.date(2023, 7, 14), datetime.date(2023, 7, 15), datetime.date(2023, 7, 16), datetime.date(2023, 7, 17), datetime.date(2023, 7, 18), datetime.date(2023, 7, 19), datetime.date(2023, 7, 20), datetime.date(2023, 7, 21), datetime.date(2023, 7, 22),
        datetime.date(2023, 7, 23), datetime.date(2023, 7, 24), datetime.date(2023, 7, 25), datetime.date(2023, 7, 26), datetime.date(2023, 7, 27), datetime.date(2023, 7, 28), datetime.date(2023, 7, 29), datetime.date(2023, 7, 30), datetime.date(2023, 7, 31), datetime.date(2023, 8, 1), datetime.date(2023, 8, 2), datetime.date(2023, 8, 3), datetime.date(2023, 8, 4), datetime.date(2023, 8, 5), datetime.date(2023, 8, 6), datetime.date(2023, 8, 7), datetime.date(2023, 8, 8),
        datetime.date(2023, 8, 9), datetime.date(2023, 8, 10), datetime.date(2023, 8, 11), datetime.date(2023, 8, 12), datetime.date(2023, 8, 13), datetime.date(2023, 8, 14), datetime.date(2023, 8, 15), datetime.date(2023, 8, 16), datetime.date(2023, 8, 17), datetime.date(2023, 8, 18), datetime.date(2023, 8, 19), datetime.date(2023, 8, 20), datetime.date(2023, 8, 21), datetime.date(2023, 8, 22),
        datetime.date(2023, 8, 23), datetime.date(2023, 8, 24), datetime.date(2023, 8, 25), datetime.date(2023, 8, 26), datetime.date(2023, 8, 27), datetime.date(2023, 8, 28), datetime.date(2023, 8, 29), datetime.date(2023, 8, 30), datetime.date(2023, 8, 31), datetime.date(2023, 9, 1), datetime.date(2023, 9, 2), datetime.date(2023, 9, 3), datetime.date(2023, 9, 4), datetime.date(2023, 9, 5), datetime.date(2023, 9, 6), datetime.date(2023, 9, 7), datetime.date(2023, 9, 8),
        datetime.date(2023, 9, 9), datetime.date(2023, 9, 10), datetime.date(2023, 9, 11), datetime.date(2023, 9, 12), datetime.date(2023, 9, 13), datetime.date(2023, 9, 14), datetime.date(2023, 9, 15), datetime.date(2023, 9, 16), datetime.date(2023, 9, 17), datetime.date(2023, 9, 18), datetime.date(2023, 9, 19), datetime.date(2023, 9, 20), datetime.date(2023, 9, 21), datetime.date(2023, 9, 22),
        datetime.date(2023, 9, 23)]

    # datetime.date(2022, 3, 1), datetime.date(2022, 3, 2), datetime.date(2022, 3, 3), datetime.date(2022, 3, 4), datetime.date(2022, 3, 5),datetime.date(2022, 3, 6),datetime.date(2022, 3, 7),datetime.date(2022, 3, 8),datetime.date(2022, 3, 9),datetime.date(2022, 3, 10),datetime.date(2022, 3, 11),datetime.date(2022, 3, 12),datetime.date(2022, 3, 13),datetime.date(2022, 3, 14),datetime.date(2022, 3, 15),
    # datetime.date(2022, 3, 16),datetime.date(2022, 3, 17),datetime.date(2022, 3, 18),datetime.date(2022, 3, 19),datetime.date(2022, 3, 20),datetime.date(2022, 3, 21),datetime.date(2022, 3, 22),datetime.date(2022, 3, 23),datetime.date(2022, 3, 24),datetime.date(2022, 3, 25),datetime.date(2022, 3, 26),datetime.date(2022, 3, 27),datetime.date(2022, 3, 28),datetime.date(2022, 3, 29),datetime.date(2022, 3, 30),datetime.date(2022, 3, 31),
    # datetime.date(2022, 4, 1), datetime.date(2022, 4, 2), datetime.date(2022, 4, 3), datetime.date(2022, 4, 4), datetime.date(2022, 4, 5),datetime.date(2022, 4, 6),datetime.date(2022, 4, 7),datetime.date(2022, 4, 8),datetime.date(2022, 4, 9),datetime.date(2022, 4, 10),datetime.date(2022, 4, 11).datetime.date(2022, 4, 12),datetime.date(2022, 4, 13),datetime.date(2022, 4, 14),datetime.date(2022, 4, 15),datetime.date(2022, 4, 16),datetime.date(2022, 4, 17),datetime.date(2022, 4, 18),datetime.date(2022, 4, 19),
    # datetime.date(2022, 4, 19),datetime.date(2022, 4, 20),datetime.date(2022, 4, 21),datetime.date(2022, 4, 22),datetime.date(2022, 4, 23),datetime.date(2022, 4, 24),datetime.date(2022, 4, 25),datetime.date(2022, 4, 26),datetime.date(2022, 4, 26),datetime.date(2022, 4, 27),datetime.date(2022, 4, 28),datetime.date(2022, 4, 29),datetime.date(2022, 4, 30),datetime.date(2022, 5, 1), datetime.date(2022, 5, 2),datetime.date(2022, 5, 3),
    # datetime.date(2022, 5, 4), datetime.date(2022, 5, 5), datetime.date(2022, 5, 6),datetime.date(2022, 5, 7),datetime.date(2022, 5, 8),datetime.date(2022, 5, 9),datetime.date(2022, 5, 10),datetime.date(2022, 5, 11),datetime.date(2022, 5, 12),datetime.date(2022, 5, 1)


    drawMin(mins, fechas)

    x = open("dataJo.txt", "a")
    for m, f in zip(mins, fechas):
        x.write(str(m))
        x.write(" ")
        x.write(str(f))
        x.write('\n')
    x.close()

    # drawAll(distance, dates, distance2, dates2)



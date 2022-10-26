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
        name='0.01',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (dias)'),
            yaxis=dict(title='Distancia (km)'),
            font=dict(size=25),
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
        name='23/09/2022',
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
            xaxis=dict(title='Fecha de salida'),
            yaxis=dict(title='Distancia (km)'),
            font=dict(size=20),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def parseParameters(file, initial_date):
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
            if count == 1: #1 es tierra 2 es venus
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
    '''
    distance23, t23, dates23, min23, fecha_min23 = parseParameters(sys.argv[1], datetime.date(2022, 9, 23))
    distance24, t24, dates24, min24, fecha_min24 = parseParameters(sys.argv[2], datetime.date(2022, 9, 24))
    distance25, t25, dates25, min25, fecha_min25 = parseParameters(sys.argv[3], datetime.date(2022, 9, 25))
    distance26, t26, dates26, min26, fecha_min26 = parseParameters(sys.argv[4], datetime.date(2022, 9, 26))
    distance27, t27, dates27, min27, fecha_min27 = parseParameters(sys.argv[5], datetime.date(2022, 9, 27))
    distance28, t28, dates28, min28, fecha_min28 = parseParameters(sys.argv[6], datetime.date(2022, 9, 28))
    distance29, t29, dates29, min29, fecha_min29 = parseParameters(sys.argv[7], datetime.date(2022, 9, 29))
    distance30, t30, dates30, min30, fecha_min30 = parseParameters(sys.argv[8], datetime.date(2022, 9, 30))
'''
    distance11, t11, dates11, min11, fecha_min11 = parseParameters(sys.argv[1], datetime.date(2023, 3, 8))
    print("min" , min11)
    print(fecha_min11)
   # draw(distance11, dates11)
    # draw(distance23, dates23)
    # draw(distance30, dates30)
    #
    # mins = [min23, min24, min25, min26, min27, min28, min29, min30]
    # fechas = [datetime.date(2022, 9, 23), datetime.date(2022, 9, 24), datetime.date(2022, 9, 25), datetime.date(2022, 9, 26), datetime.date(2022, 9, 27), datetime.date(2022, 9, 28), datetime.date(2022, 9, 29), datetime.date(2022, 9, 30)]
    # drawMin(mins, fechas)

    # drawAll(distance, dates, distance2, dates2)




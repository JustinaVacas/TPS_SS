import sys
import plotly.graph_objects as go
import numpy as np

def linear(data1):

    data = []

    data.append(go.Scatter(
        x=data1[0], y=data1[1],
        mode='lines+markers',
        name='50',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Pendiente'),
            yaxis=dict(title='Error Cuadrático'),
            font=dict(size=18),
            plot_bgcolor='rgb(255,255,255)'

        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def draw(data1):

    data = []

    data.append(go.Scatter(
        x=data1[0], y=data1[1],
        mode='lines+markers',
        name='50',
        marker={'color':'orange'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Temperatura (J)'),
            yaxis=dict(title='Presion (N/m)'),
            font=dict(size=18),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    J = 0
    time = 0
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 2:
            continue
        elif len(split) == 3:
            J += abs(float(split[2]))
            time = float(split[0])
        elif len(split) == 4:
            continue
    L = 0.09*2 + 0.24*2         # el perimetro
    return J/(time*L)


if __name__ == '__main__':
    presion1 = parseParameters(sys.argv[1])
    presion2 = parseParameters(sys.argv[2])
    presion3 = parseParameters(sys.argv[3])
    presiones = [presion1, presion2, presion3]

    temp1 = (1/2)*0.01*0.01*1
    temp2 = (1/2)*0.02*0.02*1
    temp3 = (1/2)*0.03*0.03*1
    temperaturas = [temp1, temp2, temp3]
    print("temp", temperaturas)
    draw([temperaturas, presiones])

    M = (presion3-presion2)/(temp3-temp2)

    m = np.linspace(M-0.1*M, M+0.1*M, 30)
    error = []
    for mi in m:
        err = 0
        for presion, temperatura in zip(presiones, temperaturas):
            err += presion - temperatura*mi
        error.append(err**2)

    linear([m, error])

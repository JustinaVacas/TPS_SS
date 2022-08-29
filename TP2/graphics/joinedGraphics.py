import sys
import plotly.graph_objects as go


def draw(dataOrder, iters, dataOrder2, dataOrder3, dataOrder4, dataOrder5, dataOrder6):

    data = []

    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder,
        mode='lines',
        name='0.1',
        marker={'color':'pink'}
    ))
    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder2,
        mode='lines',
        name='0.5',
        marker={'color':'green'}
    ))
    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder3,
        mode='lines',
        name='1',
        marker={'color':'purple'}
    ))
    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder4,
        mode='lines',
        name='1.5',
        marker={'color':'orange'}
    ))
    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder5,
        mode='lines',
        name='2',
        marker={'color':'blue'}
    ))
    data.append(go.Scatter(
        x=list(range(1, iters + 1)), y=dataOrder6,
        mode='lines',
        name='2.5',
        marker={'color':'red'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Iteraciones'),
            yaxis=dict(title='Orden'),
            legend=dict(title='Eta')
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.show()


def parseParameters(file):
    with open(file) as ordersFile:
        ordersLines = ordersFile.readlines()

    birds = []
    for line in ordersLines:
        newline = line.strip()
        birds.append(float(newline))

    return birds, len(birds)


if __name__ == '__main__':
    birds, iterations = parseParameters(sys.argv[1])
    birds2, iterations2 = parseParameters(sys.argv[2])
    birds3, iterations3 = parseParameters(sys.argv[3])
    birds4, iterations4 = parseParameters(sys.argv[4])
    birds5, iterations5 = parseParameters(sys.argv[5])
    birds6, iterations6 = parseParameters(sys.argv[6])
    draw(birds, iterations, birds2, birds3, birds4, birds5, birds6)
